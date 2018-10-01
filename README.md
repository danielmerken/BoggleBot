# BoggleBot
This project encapsulates a bot that can solve a Boggle board of various sizes. To increase efficency when searching for words, a trie is used as to hold the dictionary. By doing this, the bot does not attempt to complete invalid prefixes. The bot performs a depth first search on each space. The bot includes a parallelized version. Speedup is trivial on small boards, but greatly increases runtime on larger boards.

| Board Dimension        | 4 x 4 | 10 x 10 | 20 x 20 | 50 x 50 | 100 x 100 |
| ---------------------- | ----- | ------- | ------- | ------- | --------- |
| Linear Time (ms)       | 0     | 1       | 0       | 3       | 2         |
| Parallelized Time (ms) | 0     | 11      | 379     | 45834   | 589158    |

This project also includes a JUnit testing suite.
