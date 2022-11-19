According to the different of similarity between the codes, clones can be divided into the following four categories:
- **Type-1**: Identical code fragments, except for differences inwhite-space, layout and comments.
- **Type-2**: Identical code fragments,except for differences inidentifier names and literal values,in addition to Type-1 clone differences.
- **Type-3**: syntactically similar code fragments that differ at the statement level. The fragments have statements added, modified and/or removed with respect to each other, in addition to Type-1 and Type-2 clone differences .
- **Type-4**: Syntactically dissimilar code fragments that implement the same functionality.

The figure below shows an example method and its WT3 clone and T4 clone. The original method takes two numbers and returns a comma-separated sequence of integers in between the two numbers, as a string.
According to the definition of clone type, the WT3 and T4 clones in the figure below are syntactically almost completely different from the original code but achieve the same function, which creates a blurred boundary between T3 and T4.
<div align="center">
<img src=img/clonetype.png width=60% />
<div>
