var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":42,"id":1905,"methods":[{"el":11,"sc":5,"sl":9},{"el":15,"sc":5,"sl":13},{"el":19,"sc":5,"sl":17},{"el":23,"sc":5,"sl":21},{"el":41,"sc":5,"sl":25}],"name":"SummaryReportViewModel","sl":5}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_114":{"methods":[{"sl":9},{"sl":13},{"sl":17},{"sl":21}],"name":"translatesResponseModelToViewModel","pass":true,"statements":[{"sl":10},{"sl":14},{"sl":18},{"sl":22}]},"test_180":{"methods":[{"sl":17},{"sl":21}],"name":"controllerPassesRequestModelToUseCaseOnUpdatePlagInfo","pass":true,"statements":[{"sl":18},{"sl":22}]},"test_238":{"methods":[{"sl":9},{"sl":13},{"sl":17},{"sl":21}],"name":"updatesPlagInfoForExistingPair","pass":true,"statements":[{"sl":10},{"sl":14},{"sl":18},{"sl":22}]},"test_502":{"methods":[{"sl":21}],"name":"controllerPassesRequestModelToUseCaseWithNoSelectedPair","pass":true,"statements":[{"sl":22}]},"test_507":{"methods":[{"sl":13},{"sl":17}],"name":"ifErrorMessageIsPresentDisplayIt","pass":true,"statements":[{"sl":14},{"sl":18}]},"test_60":{"methods":[{"sl":9},{"sl":13}],"name":"showsErrorForNonExistingPair","pass":true,"statements":[{"sl":10},{"sl":14}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [60, 114, 238], [60, 114, 238], [], [], [60, 114, 238, 507], [60, 114, 238, 507], [], [], [114, 238, 507, 180], [114, 238, 507, 180], [], [], [114, 238, 502, 180], [114, 238, 502, 180], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []]
