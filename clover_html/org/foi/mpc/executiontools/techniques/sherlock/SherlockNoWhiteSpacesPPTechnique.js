var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":9,"id":884,"methods":[{"el":8,"sc":5,"sl":6}],"name":"SherlockNoWhiteSpacesPPTechnique","sl":3}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_437":{"methods":[{"sl":6}],"name":"canRunMultipleDetectionOnStudentsDirs","pass":true,"statements":[{"sl":7}]},"test_441":{"methods":[{"sl":6}],"name":"runDetectionWithComboTechnique","pass":true,"statements":[{"sl":7}]},"test_636":{"methods":[{"sl":6}],"name":"canRunAllToolsOnAllTehcniques","pass":true,"statements":[{"sl":7}]},"test_721":{"methods":[{"sl":6}],"name":"canRunCombinationsOfTechniques","pass":true,"statements":[{"sl":7}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [437, 721, 636, 441], [437, 721, 636, 441], [], []]
