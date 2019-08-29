var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":83,"id":8784,"methods":[{"el":25,"sc":5,"sl":22},{"el":31,"sc":5,"sl":27},{"el":37,"sc":5,"sl":33},{"el":47,"sc":5,"sl":39},{"el":57,"sc":5,"sl":49}],"name":"RenamerTest","sl":19},{"el":82,"id":8806,"methods":[{"el":67,"sc":9,"sl":63},{"el":72,"sc":9,"sl":69},{"el":81,"sc":9,"sl":74}],"name":"RenamerTest.withRealDir","sl":59}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_220":{"methods":[{"sl":33}],"name":"runWithFile","pass":true,"statements":[{"sl":35},{"sl":36}]},"test_361":{"methods":[{"sl":39}],"name":"canRename","pass":true,"statements":[{"sl":41},{"sl":42},{"sl":43},{"sl":44},{"sl":45},{"sl":46}]},"test_460":{"methods":[{"sl":74}],"name":"canRenameRealDir","pass":true,"statements":[{"sl":76},{"sl":77},{"sl":78},{"sl":79},{"sl":80}]},"test_568":{"methods":[{"sl":49}],"name":"invalidNamesStored","pass":true,"statements":[{"sl":51},{"sl":52},{"sl":53},{"sl":54},{"sl":55},{"sl":56}]},"test_706":{"methods":[{"sl":27}],"name":"isPreparerToolAndHasCorecctName","pass":true,"statements":[{"sl":29},{"sl":30}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [706], [], [706], [706], [], [], [220], [], [220], [220], [], [], [361], [], [361], [361], [361], [361], [361], [361], [], [], [568], [], [568], [568], [568], [568], [568], [568], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [460], [], [460], [460], [460], [460], [460], [], [], []]
