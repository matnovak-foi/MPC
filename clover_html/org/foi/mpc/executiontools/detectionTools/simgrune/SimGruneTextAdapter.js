var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":27,"id":5313,"methods":[{"el":12,"sc":5,"sl":10},{"el":16,"sc":5,"sl":14},{"el":21,"sc":5,"sl":18},{"el":26,"sc":5,"sl":23}],"name":"SimGruneTextAdapter","sl":6}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_130":{"methods":[{"sl":18}],"name":"isExecutionPhaseAndHasCorrectGetName","pass":true,"statements":[{"sl":20}]},"test_437":{"methods":[{"sl":10},{"sl":18},{"sl":23}],"name":"canRunMultipleDetectionOnStudentsDirs","pass":true,"statements":[{"sl":11},{"sl":20},{"sl":25}]},"test_439":{"methods":[{"sl":14}],"name":"defaultOptionsAreOk","pass":true,"statements":[{"sl":15}]},"test_636":{"methods":[{"sl":10},{"sl":18},{"sl":23}],"name":"canRunAllToolsOnAllTehcniques","pass":true,"statements":[{"sl":11},{"sl":20},{"sl":25}]},"test_663":{"methods":[{"sl":10}],"name":"createsToolObjectBasedOnName","pass":true,"statements":[{"sl":11}]},"test_699":{"methods":[{"sl":10}],"name":"createsSIMTextWithConfigFromFile","pass":true,"statements":[{"sl":11}]},"test_705":{"methods":[{"sl":23}],"name":"programIsCreatedAndRunedWithRunToolMethod","pass":true,"statements":[{"sl":25}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [636, 437, 699, 663], [636, 437, 699, 663], [], [], [439], [439], [], [], [636, 130, 437], [], [636, 130, 437], [], [], [636, 705, 437], [], [636, 705, 437], [], []]
