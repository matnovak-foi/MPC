var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":42,"id":9,"methods":[{"el":28,"sc":5,"sl":14},{"el":41,"sc":5,"sl":30}],"name":"MPCMatchPart","sl":5}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_156":{"methods":[{"sl":14}],"name":"readsTwoMPCFilesAddPassesItToListener","pass":true,"statements":[{"sl":16},{"sl":17},{"sl":18}]},"test_312":{"methods":[{"sl":14}],"name":"readsTwoMPCFilesAddPassesItToListener","pass":true,"statements":[{"sl":16},{"sl":17},{"sl":18}]},"test_328":{"methods":[{"sl":14}],"name":"readsTwoMPCFilesAddPassesItToListener","pass":true,"statements":[{"sl":16},{"sl":17},{"sl":18}]},"test_492":{"methods":[{"sl":14}],"name":"readsTwoMPCFilesAddPassesItToListener","pass":true,"statements":[{"sl":16},{"sl":17},{"sl":18}]},"test_611":{"methods":[{"sl":14}],"name":"readsTwoMPCFilesAddPassesItToListener","pass":true,"statements":[{"sl":16},{"sl":17},{"sl":18}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [328, 611, 492, 312, 156], [], [328, 611, 492, 312, 156], [328, 611, 492, 312, 156], [328, 611, 492, 312, 156], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []]
