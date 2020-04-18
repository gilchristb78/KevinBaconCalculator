import csv
import sys

from Utility import Node, StackFrontier, QueueFrontier

# Maps names to a set of corresponding person_ids
names = {}

# Maps person_ids to a dictionary of: name, birth, movies (a set of movie_ids)
people = {}

# Maps movie_ids to a dictionary of: title, year, stars (a set of person_ids)
movies = {}


def load_data(directory):
    """
    Load data from CSV files into memory.
    """
    # Load people
    with open(f"{directory}/people.csv", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            people[row["id"]] = {
                "name": row["name"],
                "birth": row["birth"],
                "movies": set()
            }
            if row["name"].lower() not in names:
                names[row["name"].lower()] = {row["id"]}
            else:
                names[row["name"].lower()].add(row["id"])

    # Load movies
    with open(f"{directory}/movies.csv", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            movies[row["id"]] = {
                "title": row["title"],
                "year": row["year"],
                "stars": set()
            }

    # Load stars
    with open(f"{directory}/stars.csv", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            try:
                people[row["person_id"]]["movies"].add(row["movie_id"])
                movies[row["movie_id"]]["stars"].add(row["person_id"])
            except KeyError:
                pass


def main():
    if len(sys.argv) > 2:
        sys.exit("Usage: python degrees.py [directory]")
    directory = sys.argv[1] if len(sys.argv) == 2 else "large"

    # Load data from files into memory
    print("Loading data...")
    load_data(directory)
    print("Data loaded.")

    source = person_id_for_name(input("Name: "))
    if source is None:
        sys.exit("Person not found.")
    target = person_id_for_name(input("Name: "))
    if target is None:
        sys.exit("Person not found.")

    path = shortest_path(source, target)

    if path is None:
        print("Not connected.")
    else:
        degrees = len(path)
        print(f"{degrees} degrees of separation.")
        path = [(None, source)] + path
        for i in range(degrees):
            person1 = people[path[i][1]]["name"]
            person2 = people[path[i + 1][1]]["name"]
            movie = movies[path[i + 1][0]]["title"]
            print(f"{i + 1}: {person1} and {person2} starred in {movie}")


def shortest_path(source, target):
    
    start = Node(state=source,parent=None,action=None)
    starterFrontier = QueueFrontier()
    starterFrontier.add(start)

    end = Node(state=target,parent=None,action=None)
    targetFrontier = QueueFrontier()
    targetFrontier.add(end)
    
    explored = set()
    ##two frontiers and find working
    while True:
        if starterFrontier.empty():
            raise Exception("No Solution")
        node = starterFrontier.remove()
        if targetFrontier.contains_state(node.state):
            node2 = targetFrontier.find(node.state)
            path = []
            while node.parent is not None:
                path.append((node.action,node.state))
                node = node.parent
            path.reverse()
            path2 = []
            while node2.parent is not None:
                path2.append((node2.action,node2.parent.state))
                node2 = node2.parent
            if len(path2) > 0:
                for tup in path2:
                    path.append(tup)
            return path
            
            ## node is starter frontier, reverse the parents array
            # add on the targetfrontier.find parents in order
            # account for double (found.state == node.state), return after double removed
        explored.add(node.state)
        for action, state in neighbors_for_person(node.state):
            if not starterFrontier.contains_state(state) and not state in explored:
                child = Node(state=state, parent=node,action=action)
                starterFrontier.add(child)
        if targetFrontier.empty():
            raise Exception("No Solution")
        node = targetFrontier.remove()
        if starterFrontier.contains_state(node.state):
            node1 = starterFrontier.find(node.state)
            path = []
            while node1.parent is not None:
                path.append((node1.action,node1.state))
                node1 = node1.parent
            path.reverse()
            path2 = []
            while node.parent is not None:
                path2.append((node.action,node.parent.state))
                node = node.parent
            
            if len(path2) > 0:
                for tup in path2:
                    path.append(tup)
            return path

            # node is target frontier, start with other one
            # find the starterfrontier.find, make parents array and reverse
            # add on node parents 
            # account for double (found.state == node.state), return that
        explored.add(node.state)
        for action, state in neighbors_for_person(node.state):
            if not targetFrontier.contains_state(state) and not state in explored:
                child = Node(state=state, parent=node,action=action)
                targetFrontier.add(child)
        
    
    
    """
    go back and forth every single iteration
    remove 1 node
    is the node state in the other one
    if so win
    otherwise add its parents to its own frontier
    go to the other frontier
    remove 1 node
    is in other
    etc.
    then once both remove 1 node and add parents repeat
     """

    

    


def person_id_for_name(name):
    """
    Returns the IMDB id for a person's name,
    resolving ambiguities as needed.
    """
    person_ids = list(names.get(name.lower(), set()))
    if len(person_ids) == 0:
        return None
    elif len(person_ids) > 1:
        print(f"Which '{name}'?")
        for person_id in person_ids:
            person = people[person_id]
            name = person["name"]
            birth = person["birth"]
            print(f"ID: {person_id}, Name: {name}, Birth: {birth}")
        try:
            person_id = input("Intended Person ID: ")
            if person_id in person_ids:
                return person_id
        except ValueError:
            pass
        return None
    else:
        return person_ids[0]


def neighbors_for_person(person_id):
    """
    Returns (movie_id, person_id) pairs for people
    who starred with a given person.
    """
    movie_ids = people[person_id]["movies"]
    neighbors = set()
    for movie_id in movie_ids:
        for person_id in movies[movie_id]["stars"]:
            neighbors.add((movie_id, person_id))
    return neighbors


if __name__ == "__main__":
    main()