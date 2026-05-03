# Neural Nexus Core AI: Cognitive Index

A custom text indexing engine built in Java using AVL trees and linked lists, designed for efficient token search, insertion, deletion, and traversal across large-scale text files.

**First team to score 100/100 on HackerRank across all sections** in CS210 Data Structures and Algorithms at Prince Sultan University.

## Overview

The Cognitive Index acts as a search engine backend that indexes tokens from up to 100,000 files. It supports four core operations:

1. **Add Files** - Index tokens from multiple files with line-level tracking
2. **Search** - Look up a token and retrieve its frequency and locations
3. **Delete** - Remove a token from the index
4. **Print** - Display all indexed tokens via traversal

## Why AVL Trees?

| Operation | AVL Tree (Ours) | Doubly Linked List | BST (Worst Case) |
|-----------|----------------|--------------------|-------------------|
| Search | O(log n) | O(n) | O(n) |
| Insertion | O(log n) | O(n) | O(n) |
| Deletion | O(log n) | O(n) | O(n) |

AVL trees guarantee O(log n) performance for all core operations through self-balancing. Unlike a standard BST, which can degrade to O(n) with sorted input, AVL trees maintain consistent performance. Unlike hash tables, AVL trees preserve ordering, enabling sequential traversal, which is essential for a search engine.

For 1 million tokens, a linked list would require ~500,000 steps on average to find a token. Our AVL-based index needs only ~20.

## How It Works

- Each AVL node stores a unique token, its frequency, and a linked list of file locations (filename + line number)
- Duplicate tokens increment the frequency counter and append to the location list
- The tree self-balances after every insertion and deletion using left/right rotations

## How to Run

```bash
javac src/Solution.java
java -cp src Solution < input.txt
```

## Input Format

```
1 <number_of_files>
<filename> <number_of_lines>
<line content>
...
2 <token>       // Search
3 <token>       // Delete
4              // Print all tokens
```

## Authors

- [Abdulrahman Solimanie](https://github.com/AbdulrahmanSoli)
- [Abdulaziz Alfadul](https://github.com/methrrd)

## Course

CS210 - Data Structures and Algorithms, Prince Sultan University (April 2025)
