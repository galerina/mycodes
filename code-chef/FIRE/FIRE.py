import sys
import collections

def calculate_weight(child_weights):
    if len(child_weights) == 0:
        return 0
    else:
        weights = sorted(child_weights,reverse=True)
        return_weight = weights[0]
        for w in weights[1:]:
            if return_weight == w:
                return_weight -= 1
            else:
                return_weight = w
        return return_weight + 1

def can_protect(graph, start, to_protect):
    parent = [0]*len(graph)

    visited = [False] * len(graph)
    queue = [start]
    level = [0] * len(graph)
    level[start] = 0
    while queue:
        current = queue.pop(0)
        if not visited[current]:
            visited[current] = True
            children = [x for x in graph[current] if not visited[x]]
            for v in children:
                parent[v] = current
                level[v] = level[current] + 1
            queue.extend(children)

    # Prune tree
    pruned_tree = [False] * len(graph)
    for v in to_protect:
        pruned_tree[v] = True
        current = v
        while parent[current] > 0 and pruned_tree[parent[current]] == False:
            pruned_tree[parent[current]] = True
            current = parent[current]

    # Group nodes into levels
    levels = collections.defaultdict(list)
    for idx, present in enumerate(pruned_tree):
        if present:
            levels[level[idx]].append(idx)

    # Starting at level farthest from root, calculate weights
    #  while climbing levels
    weights = [-1] * len(graph)
    for key in sorted(levels.keys(),reverse=True):
        for v in levels[key]:
            children = [x for x in graph[v] if not x == parent[v] and pruned_tree[x]]
            weights[v] = calculate_weight([weights[c] for c in children])

    # print weights
    # print parent
    return weights[start] > 0

if __name__=="__main__":
    lines = sys.stdin.readlines()
    T = int(lines[0])

    current_idx = 1
    for i in range(T):
        try:
            current_idx += 1 # skip blank line
            n,s,t = [int(x) for x in lines[current_idx].split()[:3]]
            current_idx += 1
            adj = [0]*(n+1)
            for j in range(n):
                numbers = lines[current_idx].split()
                a = [int(x) for x in numbers[1:1+int(numbers[0])]]
                current_idx += 1
                adj[j+1] = set(a)
            to_protect = [int(x) for x in lines[current_idx].split()[:t]]
            current_idx += 1
        except ValueError:
            print "no"

        if can_protect(adj,s,to_protect):
            print "yes"
        else:
            print "no"

