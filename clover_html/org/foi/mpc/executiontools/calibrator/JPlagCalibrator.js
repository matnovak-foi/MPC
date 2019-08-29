var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":42,"id":3183,"methods":[{"el":15,"sc":5,"sl":13},{"el":23,"sc":5,"sl":17},{"el":30,"sc":5,"sl":25},{"el":41,"sc":5,"sl":32}],"name":"JPlagCalibrator","sl":11}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_347":{"methods":[{"sl":17},{"sl":25}],"name":"invalidCalibrationCaseDirName","pass":true,"statements":[{"sl":19},{"sl":20},{"sl":21},{"sl":26},{"sl":27},{"sl":28},{"sl":29}]},"test_632":{"methods":[{"sl":17},{"sl":25},{"sl":32}],"name":"findCorrectSimilarity","pass":true,"statements":[{"sl":19},{"sl":20},{"sl":21},{"sl":26},{"sl":27},{"sl":28},{"sl":29},{"sl":34},{"sl":35},{"sl":36},{"sl":37},{"sl":38},{"sl":39},{"sl":40}]},"test_707":{"methods":[{"sl":17},{"sl":25},{"sl":32}],"name":"noCorrectMatchRunAll29Iterations","pass":true,"statements":[{"sl":19},{"sl":20},{"sl":21},{"sl":26},{"sl":27},{"sl":28},{"sl":29},{"sl":34},{"sl":35},{"sl":36},{"sl":37},{"sl":38},{"sl":39},{"sl":40}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [707, 347, 632], [], [707, 347, 632], [707, 347, 632], [707, 347, 632], [], [], [], [707, 347, 632], [707, 347, 632], [707, 347, 632], [707, 347, 632], [707, 347, 632], [], [], [707, 632], [], [707, 632], [707, 632], [707, 632], [707, 632], [707, 632], [707, 632], [707, 632], [], []]
