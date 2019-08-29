var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":22,"id":13247,"methods":[{"el":16,"sc":5,"sl":14},{"el":21,"sc":5,"sl":18}],"name":"PhaseRunnerBuilderDummy","sl":12}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_150":{"methods":[{"sl":18}],"name":"withNoMatch","pass":true,"statements":[{"sl":20}]},"test_155":{"methods":[{"sl":18}],"name":"containsInfoAboutDiffForEveryParamCombo","pass":true,"statements":[{"sl":20}]},"test_184":{"methods":[{"sl":18}],"name":"withOneDirAndThreeIterationsLastIsBest","pass":true,"statements":[{"sl":20}]},"test_357":{"methods":[{"sl":18}],"name":"withTwoDirsAndTwoIterationLastIsBestForBoth","pass":true,"statements":[{"sl":20}]},"test_531":{"methods":[{"sl":18}],"name":"withTwoDirsAndThreeIterationsMiddleIsOptimalAndForNoneBest","pass":true,"statements":[{"sl":20}]},"test_687":{"methods":[{"sl":18}],"name":"withTwoDirsAndOneIteration","pass":true,"statements":[{"sl":20}]},"test_755":{"methods":[{"sl":18}],"name":"withTwoDirsAndThreeIterationsMiddleIsBestForBoth","pass":true,"statements":[{"sl":20}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [687, 150, 755, 357, 531, 155, 184], [], [687, 150, 755, 357, 531, 155, 184], [], []]
