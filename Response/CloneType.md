## Clone Type
According to the different of similarity between the codes, clones can be divided into the following four categories:
- **Type-1**: Identical code fragments, except for differences inwhite-space, layout and comments.
- **Type-2**: Identical code fragments,except for differences inidentifier names and literal values,in addition to Type-1 clone differences.
- **Type-3**: syntactically similar code fragments that differ at the statement level. The fragments have statements added, modified and/or removed with respect to each other, in addition to Type-1 and Type-2 clone differences .
- **Type-4**: Syntactically dissimilar code fragments that implement the same functionality.

T3 and T4 clones have always been difficult to clearly classify. In order to more clearly define the boundaries between T3 and T4, BCB divided the types after T3 into VST3, ST3, MT3, and WT3/T4.  However, their classification method is coarse-grained and only considers the similarity of Token level. On this basis, we propose TreeDiff to divide T3 and T4 in a more granular and comprehensive manner

The figure below shows an example method and different clones of it. The original method takes two numbers and returns a comma-separated sequence of integers in between the two numbers, as a string.
According to the definition of clone type, the MT3, WT3 and T4 clones in the figure below are syntactically almost completely different from the original code but achieve the same function, which creates a blurred boundary between T3 and T4.
<div align="center">
<img src=img/clonetype.png width=80% >
</div>

***
<!-- <p align="left"> -->

## Table 4,5,6

Table 4 presents the recall results for combinations of token-based algorithms.

Table 5 presents the recall results for combinations of AST-based algorithms.

Table 6 shows the recall results of a2 and a4 when t2 and t3 with different thresholds are used as the filter.
<!-- </p> -->
<div align="center">
<img src=img/tab45.png width=80% />
<div>
<div align="center">
<img src=img/tab6.png width=80% />
<div>
