var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":151,"id":7419,"methods":[{"el":70,"sc":5,"sl":36},{"el":75,"sc":5,"sl":72},{"el":98,"sc":5,"sl":77},{"el":119,"sc":5,"sl":100},{"el":126,"sc":5,"sl":121},{"el":132,"sc":5,"sl":128},{"el":143,"sc":5,"sl":134},{"el":150,"sc":5,"sl":145}],"name":"SherlockCalibratorTest","sl":27}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_270":{"methods":[{"sl":77},{"sl":134},{"sl":145}],"name":"findCorrectSimilarityOnFirstRun","pass":true,"statements":[{"sl":79},{"sl":80},{"sl":82},{"sl":83},{"sl":84},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":90},{"sl":91},{"sl":92},{"sl":93},{"sl":94},{"sl":95},{"sl":96},{"sl":136},{"sl":137},{"sl":138},{"sl":140},{"sl":141},{"sl":142},{"sl":146},{"sl":147},{"sl":148},{"sl":149}]},"test_410":{"methods":[{"sl":121}],"name":"noCorrectMatchRunAllIterations","pass":true,"statements":[{"sl":123},{"sl":124},{"sl":125}]},"test_591":{"methods":[{"sl":128},{"sl":134},{"sl":145}],"name":"invalidCalibrationCaseDirName","pass":true,"statements":[{"sl":130},{"sl":131},{"sl":136},{"sl":137},{"sl":140},{"sl":141},{"sl":142},{"sl":146},{"sl":147},{"sl":148},{"sl":149}]},"test_667":{"methods":[{"sl":100},{"sl":134},{"sl":145}],"name":"findCorrectSimilarityOnSecondRun","pass":true,"statements":[{"sl":102},{"sl":103},{"sl":105},{"sl":106},{"sl":107},{"sl":108},{"sl":109},{"sl":110},{"sl":111},{"sl":112},{"sl":113},{"sl":114},{"sl":115},{"sl":116},{"sl":117},{"sl":118},{"sl":136},{"sl":137},{"sl":138},{"sl":140},{"sl":141},{"sl":142},{"sl":146},{"sl":147},{"sl":148},{"sl":149}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [270], [], [270], [270], [], [270], [270], [270], [270], [270], [270], [270], [270], [270], [270], [270], [270], [270], [270], [270], [], [], [], [667], [], [667], [667], [], [667], [667], [667], [667], [667], [667], [667], [667], [667], [667], [667], [667], [667], [667], [], [], [410], [], [410], [410], [410], [], [], [591], [], [591], [591], [], [], [591, 667, 270], [], [591, 667, 270], [591, 667, 270], [667, 270], [], [591, 667, 270], [591, 667, 270], [591, 667, 270], [], [], [591, 667, 270], [591, 667, 270], [591, 667, 270], [591, 667, 270], [591, 667, 270], [], []]
