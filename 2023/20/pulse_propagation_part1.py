# Pulse Propagation Part #1

import abc
from queue import Queue


class EventBus:
    queue: Queue["Pulse"]
    count_high: int
    count_low: int

    def __init__(self):
        self.queue = Queue()
        self.count_high = 0
        self.count_low = 0
    
    def add(self, pulse: "Pulse"):
        self.queue.put(pulse)
        if pulse.value == False:
            self.count_low += 1
        elif pulse.value == True:
            self.count_high += 1
    
    def counts(self) -> int:
        return self.count_high * self.count_low


class PulseModule(abc.ABC):
    name: str
    listeners: list[str]
    bus: EventBus

    def __init__(self, name: str, bus: EventBus) -> None:
        self.name = name
        self.bus = bus
        self.listeners = []
    
    def __str__(self) -> str:
        return f"{self.name}({self.__class__.__name__})"

    def listen(self, name: str) -> None:
        self.listeners.append(name)

    def send(self, value: bool) -> None:
        for l in self.listeners:
            pulse = Pulse(value, self.name, l)
            self.bus.add(pulse)
    
    def receive(self, pulse: "Pulse") -> None:
        raise NotImplementedError()


class Pulse:
    value: bool
    sender: str
    recipient: str

    def __init__(self, value: bool, sender: str, recipient: str):
        self.value = value
        self.sender = sender
        self.recipient = recipient
    
    def __str__(self) -> str:
        return f"{self.sender} -{'high' if self.value else 'low'}-> {self.recipient}"


class FlipFlop(PulseModule):
    status: bool

    def __init__(self, name: str, bus: EventBus) -> None:
        super().__init__(name, bus)
        self.status = False
    
    def receive(self, pulse: Pulse):
        if pulse.value == False:
            self.status = not self.status
            self.send(self.status)


class Conjunction(PulseModule):
    inputs: dict[str, bool]

    def __init__(self, name: str, bus: EventBus) -> None:
        super().__init__(name, bus)
        self.inputs = {}

    def register_input(self, name: str):
        self.inputs[name] = False
    
    def receive(self, pulse: Pulse):
        self.inputs[pulse.sender] = pulse.value
        if all(self.inputs.values()):
            self.send(False)
        else:
            self.send(True)


class Broadcaster(PulseModule):
    def receive(self, pulse: Pulse):
        self.send(pulse.value)


def main(filename: str, presses: int):
    modules: dict[str, PulseModule] = {}
    bus = EventBus()

    # parse input
    with open(filename, "r") as file:
        while line:= file.readline().strip():
            emitter_name, listener_names = line.split(" -> ")

            emitter: PulseModule
            if 'broadcaster' == emitter_name:
                emitter = Broadcaster(emitter_name, bus)
            elif emitter_name.startswith('%'):
                emitter_name = emitter_name[1:]
                emitter = FlipFlop(emitter_name, bus)
            else:
                emitter_name = emitter_name[1:]
                emitter = Conjunction(emitter_name, bus)
            
            for listener_name in listener_names.split(', '):
                emitter.listen(listener_name)

            modules[emitter_name] = emitter
        
    # run through all modules, and register inputs for Conjunction modules
    for name, obj in modules.items():
        for l in obj.listeners:
            listener = modules.get(l)
            if isinstance(listener, Conjunction):
                listener.register_input(name)
    
    # push the button, send out first low pulse to broadcaster
    for p in range(0, presses):
        bus.add(Pulse(False, 'button', 'broadcaster'))

        # run through the queue
        while not bus.queue.empty():
            pulse = bus.queue.get()
            #print(pulse)
            try:
                m = modules[pulse.recipient]
                m.receive(pulse)
            except KeyError:
                pass
        #print("----")
    
    print(bus.counts())



if __name__ == "__main__":
    main("./input.txt", 1000)