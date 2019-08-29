var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":16,"id":544,"methods":[{"el":10,"sc":5,"sl":7},{"el":15,"sc":5,"sl":12}],"name":"InMemoryObjectFileUtility","sl":6}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_225":{"methods":[{"sl":12}],"name":"readerCallsListenerForEveryDirAndEveryFileInDir","pass":true,"statements":[{"sl":14}]},"test_762":{"methods":[{"sl":12}],"name":"readerCallsListenerForEveryDirAndEveryFileInDir","pass":true,"statements":[{"sl":14}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [225, 762], [], [225, 762], [], []]
