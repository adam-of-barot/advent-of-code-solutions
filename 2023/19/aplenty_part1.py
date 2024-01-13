# Aplenty Part #1

from ast import literal_eval

class Part:
    ratings: dict[str, int]

    def __init__(self, init_string: str) -> None:
        init_string = init_string.replace("{", "").replace("}", "")
        ratings_str_list = init_string.split(",")
        self.ratings = {}
        for r in ratings_str_list:
            rating, value = r.split("=")
            self.ratings[rating] = int(value)
    
    def sum(self):
        s = 0
        for v in self.ratings.values():
            s += v
        return s


class Rule:
    prop: str | None
    operator: str | None
    criteria: int | None
    action: str

    def __init__(self, init_string: str) -> None:
        if ":" in init_string:
            crit, action = init_string.split(":", 2)
            self.action = action
            self.prop = crit[0]
            self.operator = crit[1]
            self.criteria = int(crit[2:])
        else:
            self.prop = ""
            self.operator = ""
            self.criteria = ""
            self.action = init_string
    
    def __str__(self) -> str:
        return f"{self.prop}{self.operator}{self.criteria}:{self.action}"

    def evaluate(self, part: Part) -> bool:
        if self.prop:
            prop = part.ratings.get(self.prop)
            if self.operator == "<":
                return prop < self.criteria
            else:
                return prop > self.criteria
        else:
            return True


class Workflow:
    id: str
    rules: list[Rule]
    
    def __init__(self, init_string: str) -> None:
        id, rules_str = init_string.split("{", 2)
        self.id = id
        self.rules = []

        rules_str = rules_str.replace("}", "")
        rules_str_list = rules_str.split(",")
        for r in rules_str_list:
            self.rules.append(Rule(r))
    

    def __str__(self) -> str:
        return self.id
    
    def run(self, part: Part) -> str:
        rule_idx = 0
        rule = self.rules[rule_idx]
        while not rule.evaluate(part):
            #print(rule)
            rule = self.rules[rule_idx]
            rule_idx += 1
        return rule.action


def main(filename: str):

    workflows: dict[str, Workflow] = {}
    parts: list[Part] = []
    accepted_ratings_sum: int = 0

    with open(filename, "r") as file:
        # parse workflows
        while line:= file.readline().strip():
            w = Workflow(line)
            workflows[w.id] = w
        
        # parse parts
        while line:= file.readline():
            p = Part(line)
            parts.append(p)
    
    for p in parts:
        w = workflows.get("in")
        #print(w)
        result = ""
        while result not in ["R", "A"]:
            result = w.run(p)
            w = workflows.get(result)
        if result == "A":
            accepted_ratings_sum += p.sum()
    
    print(accepted_ratings_sum)


if __name__ == "__main__":
    main("./input.txt")