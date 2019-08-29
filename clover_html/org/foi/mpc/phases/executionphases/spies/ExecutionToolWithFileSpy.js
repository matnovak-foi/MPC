var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":33,"id":7712,"methods":[{"el":17,"sc":5,"sl":15},{"el":22,"sc":5,"sl":19},{"el":32,"sc":5,"sl":24}],"name":"ExecutionToolWithFileSpy","sl":10}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_110":{"methods":[{"sl":19},{"sl":24}],"name":"outputDirInSequentialIsLastToolDirList","pass":true,"statements":[{"sl":21},{"sl":26},{"sl":27},{"sl":28}]},"test_31":{"methods":[{"sl":19},{"sl":24}],"name":"actualCopyOfSourceDirWasPerformedIndividual","pass":true,"statements":[{"sl":21},{"sl":26},{"sl":27},{"sl":28}]},"test_473":{"methods":[{"sl":15},{"sl":19}],"name":"outputDirOfFirstIsInputDirOfSecond","pass":true,"statements":[{"sl":16},{"sl":21}]},"test_489":{"methods":[{"sl":19},{"sl":24}],"name":"actualCopyOfSourceDirWasPerformedSequential","pass":true,"statements":[{"sl":21},{"sl":26},{"sl":27},{"sl":28}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [473], [473], [], [], [31, 489, 473, 110], [], [31, 489, 473, 110], [], [], [31, 489, 110], [], [31, 489, 110], [31, 489, 110], [31, 489, 110], [], [], [], [], []]
