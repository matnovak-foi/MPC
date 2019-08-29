var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":29,"id":4299,"methods":[{"el":20,"sc":5,"sl":11},{"el":28,"sc":5,"sl":22}],"name":"ComboTechniqueResponseModel","sl":5}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_107":{"methods":[{"sl":11}],"name":"createComboTechniqueAndClearComboParts","pass":true,"statements":[{"sl":13},{"sl":14},{"sl":16},{"sl":18},{"sl":19}]},"test_112":{"methods":[{"sl":11}],"name":"createComboTechniqueWithDifferentNameSamePartsNoError","pass":true,"statements":[{"sl":13},{"sl":14},{"sl":16},{"sl":18},{"sl":19}]},"test_315":{"methods":[{"sl":11}],"name":"createComboTechniqueFromOnePartReturnError","pass":true,"statements":[{"sl":13},{"sl":14},{"sl":16},{"sl":18},{"sl":19}]},"test_344":{"methods":[{"sl":11}],"name":"createComboTechniqueFromEmptyPartsReturnError","pass":true,"statements":[{"sl":13},{"sl":14},{"sl":16},{"sl":18},{"sl":19}]},"test_469":{"methods":[{"sl":11}],"name":"createComboTechniqueFromEmptyNameReturnError","pass":true,"statements":[{"sl":13},{"sl":14},{"sl":16},{"sl":18},{"sl":19}]},"test_751":{"methods":[{"sl":11}],"name":"tryCreateComboTechniqueWithSameNameRetrunError","pass":true,"statements":[{"sl":13},{"sl":14},{"sl":16},{"sl":18},{"sl":19}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [112, 107, 469, 315, 344, 751], [], [112, 107, 469, 315, 344, 751], [112, 107, 469, 315, 344, 751], [], [112, 107, 469, 315, 344, 751], [], [112, 107, 469, 315, 344, 751], [112, 107, 469, 315, 344, 751], [], [], [], [], [], [], [], [], [], []]
