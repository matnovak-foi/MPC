var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":27,"id":3459,"methods":[{"el":15,"sc":5,"sl":13},{"el":20,"sc":5,"sl":17},{"el":25,"sc":5,"sl":22}],"name":"JPlagTextAdapter","sl":9}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_16":{"methods":[{"sl":13}],"name":"createsJPlagTextWithConfigFromFile","pass":true,"statements":[{"sl":14}]},"test_438":{"methods":[{"sl":17}],"name":"isExecutionPhaseAndHasCorrectGetName","pass":true,"statements":[{"sl":19}]},"test_636":{"methods":[{"sl":13},{"sl":17},{"sl":22}],"name":"canRunAllToolsOnAllTehcniques","pass":true,"statements":[{"sl":14},{"sl":19},{"sl":24}]},"test_652":{"methods":[{"sl":22}],"name":"programIsCreatedAndRunedWithRunToolMethod","pass":true,"statements":[{"sl":24}]},"test_663":{"methods":[{"sl":13}],"name":"createsToolObjectBasedOnName","pass":true,"statements":[{"sl":14}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [16, 636, 663], [16, 636, 663], [], [], [438, 636], [], [438, 636], [], [], [652, 636], [], [652, 636], [], [], []]
