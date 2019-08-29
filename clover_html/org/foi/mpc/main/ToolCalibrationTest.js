var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":156,"id":9994,"methods":[{"el":39,"sc":5,"sl":33},{"el":44,"sc":5,"sl":41},{"el":50,"sc":5,"sl":46},{"el":56,"sc":5,"sl":52},{"el":141,"sc":5,"sl":127},{"el":155,"sc":5,"sl":143}],"name":"ToolCalibrationTest","sl":27},{"el":125,"id":10006,"methods":[{"el":72,"sc":17,"sl":69},{"el":78,"sc":9,"sl":66},{"el":86,"sc":9,"sl":80},{"el":93,"sc":9,"sl":88},{"el":109,"sc":9,"sl":95},{"el":117,"sc":9,"sl":111},{"el":124,"sc":9,"sl":119}],"name":"ToolCalibrationTest.withUseCaseSelfShunt","sl":59}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_129":{"methods":[{"sl":69},{"sl":80},{"sl":119}],"name":"canReadProperties","pass":true,"statements":[{"sl":82},{"sl":84},{"sl":85},{"sl":121},{"sl":122},{"sl":123}]},"test_34":{"methods":[{"sl":95}],"name":"ifWrongParamsAreGivenReturnError","pass":true,"statements":[{"sl":97},{"sl":98},{"sl":100},{"sl":101},{"sl":102},{"sl":104},{"sl":105},{"sl":107}]},"test_411":{"methods":[{"sl":69},{"sl":88},{"sl":119}],"name":"passesPresenterToUseCase","pass":true,"statements":[{"sl":90},{"sl":92},{"sl":121},{"sl":122},{"sl":123}]},"test_434":{"methods":[{"sl":52}],"name":"createsPresenter","pass":true,"statements":[{"sl":54},{"sl":55}]},"test_436":{"methods":[{"sl":46}],"name":"createsUseCase","pass":true,"statements":[{"sl":48},{"sl":49}]},"test_609":{"methods":[{"sl":69},{"sl":111},{"sl":119}],"name":"passesCorrectRequestModelToUseCase","pass":true,"statements":[{"sl":113},{"sl":115},{"sl":116},{"sl":121},{"sl":122},{"sl":123}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [436], [], [436], [436], [], [], [434], [], [434], [434], [], [], [], [], [], [], [], [], [], [], [], [], [], [129, 411, 609], [], [], [], [], [], [], [], [], [], [], [129], [], [129], [], [129], [129], [], [], [411], [], [411], [], [411], [], [], [34], [], [34], [34], [], [34], [34], [34], [], [34], [34], [], [34], [], [], [], [609], [], [609], [], [609], [609], [], [], [129, 411, 609], [], [129, 411, 609], [129, 411, 609], [129, 411, 609], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []]
