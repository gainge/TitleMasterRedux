# Title Master (Redux)

This project was created to help in making video thumbnails for the [BYU Melee youtube channel](https://www.youtube.com/channel/UCkegb1U7uIpdiJdcQqxIgJQ).

Making thumbnails is easier when I don't have to jump back and forth between player tags, constantly deleting and re-creating the same base images.  This tool uses a Branch and Bound based Traveling Salesman solver (see: [youtube](https://www.youtube.com/watch?v=nN4K8xA8ShM)) to find a least-cost ordering of of match titles based on which players are featured.  As an example, the following input: 
```
Wumph vs Biz
Requiem vs Stitch
Kosmic vs Biz
Cola vs Stitch
Judge vs Jet
Wumph vs Zylix
Kosmic vs Biz
Judge vs Feenex
Ark vs OkayP
Frankie vs Kosmic
Cola vs Yikes
Wumph vs OkayP
```
results in the re-ordered list:
```
frankie vs kosmic
judge vs jet
judge vs feenex
requiem vs stitch
cola vs stitch
cola vs yikes
ark vs okayp
wumph vs okayp
wumph vs zylix
wumph vs biz
kosmic vs biz
kosmic vs biz
```
one of the possible least-cost traversals of the lists.  "Cost" is determined by how similar the positioning (i.e. player 1 or player 2) and content of the player tags are.



#### Paths Instead of Cycles
Note is that this isn't a standard instance of TSP, as the resulting list is *not* a cycle in the graph we create.  Instead, it's simply a minimum traversal of the graph, visiting every node once but not returning to the start.

This is done by including a "dummy" city in the TSP algorithm that is immediately accessible from anywhere on the graph (see [this](https://stackoverflow.com/questions/6733999/what-is-the-problem-name-for-traveling-salesman-problemtsp-without-considering/7158721#7158721) StackOverflow question).  This allows the subgraph between the dummy endpoints to serve as the optimal path.