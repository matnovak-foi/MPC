var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":20,"id":1667,"methods":[{"el":19,"sc":5,"sl":10}],"name":"SimilarityDetectionToolCalibratorFactory","sl":9}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_642":{"methods":[{"sl":10}],"name":"calibrationSIMTestWorksCase1","pass":true,"statements":[{"sl":11},{"sl":12}]},"test_679":{"methods":[{"sl":10}],"name":"calibrationSIMTestSelfWorksCase","pass":true,"statements":[{"sl":11},{"sl":12}]},"test_718":{"methods":[{"sl":10}],"name":"calibrationSIMTestWorksCase2","pass":true,"statements":[{"sl":11},{"sl":12}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [718, 642, 679], [718, 642, 679], [718, 642, 679], [], [], [], [], [], [], [], []]
