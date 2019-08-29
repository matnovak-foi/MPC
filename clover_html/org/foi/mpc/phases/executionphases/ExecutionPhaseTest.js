var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":85,"id":11197,"methods":[{"el":31,"sc":5,"sl":24},{"el":37,"sc":5,"sl":33},{"el":42,"sc":5,"sl":39},{"el":47,"sc":5,"sl":44},{"el":52,"sc":5,"sl":49},{"el":57,"sc":5,"sl":54},{"el":66,"sc":5,"sl":59},{"el":73,"sc":5,"sl":68}],"name":"ExecutionPhaseTest","sl":17},{"el":84,"id":11223,"methods":[{"el":79,"sc":9,"sl":77},{"el":83,"sc":9,"sl":81}],"name":"ExecutionPhaseTest.ExecutionPhaseStub","sl":75}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_209":{"methods":[{"sl":54}],"name":"sourceDirDoesNotExist","pass":true,"statements":[{"sl":56}]},"test_23":{"methods":[{"sl":59},{"sl":77},{"sl":81}],"name":"inputDirectoryIsEmpty_workingDirHasEmptyPhaseDir","pass":true,"statements":[{"sl":61},{"sl":62},{"sl":63},{"sl":64},{"sl":65},{"sl":78}]},"test_273":{"methods":[{"sl":44}],"name":"sourceDirIsNull","pass":true,"statements":[{"sl":46}]},"test_42":{"methods":[{"sl":49}],"name":"workingDirDoesNotExist","pass":true,"statements":[{"sl":51}]},"test_452":{"methods":[{"sl":68},{"sl":77}],"name":"workingDirOfPhaseIsNotEmptyNoError","pass":true,"statements":[{"sl":70},{"sl":71},{"sl":72},{"sl":78}]},"test_540":{"methods":[{"sl":39}],"name":"workingDirIsNull","pass":true,"statements":[{"sl":41}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [540], [], [540], [], [], [273], [], [273], [], [], [42], [], [42], [], [], [209], [], [209], [], [], [23], [], [23], [23], [23], [23], [23], [], [], [452], [], [452], [452], [452], [], [], [], [], [452, 23], [452, 23], [], [], [23], [], [], [], []]
