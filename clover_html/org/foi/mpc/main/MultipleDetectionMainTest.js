var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":172,"id":11911,"methods":[{"el":60,"sc":5,"sl":51},{"el":65,"sc":5,"sl":62},{"el":80,"sc":5,"sl":67},{"el":87,"sc":5,"sl":82},{"el":95,"sc":5,"sl":89},{"el":102,"sc":5,"sl":97},{"el":119,"sc":5,"sl":104},{"el":129,"sc":5,"sl":121},{"el":135,"sc":5,"sl":131},{"el":142,"sc":5,"sl":137},{"el":150,"sc":5,"sl":144},{"el":158,"sc":5,"sl":152},{"el":171,"sc":5,"sl":160}],"name":"MultipleDetectionMainTest","sl":40}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_309":{"methods":[{"sl":67}],"name":"ifWrongParamsAreGivenReturnError","pass":true,"statements":[{"sl":69},{"sl":70},{"sl":72},{"sl":73},{"sl":75},{"sl":76},{"sl":78}]},"test_356":{"methods":[{"sl":82},{"sl":131},{"sl":137}],"name":"canReadPropertiesFile","pass":true,"statements":[{"sl":84},{"sl":86},{"sl":133},{"sl":134},{"sl":139},{"sl":140},{"sl":141}]},"test_437":{"methods":[{"sl":160}],"name":"canRunMultipleDetectionOnStudentsDirs","pass":true,"statements":[{"sl":161},{"sl":162},{"sl":163},{"sl":164},{"sl":165},{"sl":166},{"sl":167},{"sl":168},{"sl":169},{"sl":170}]},"test_596":{"methods":[{"sl":104},{"sl":131},{"sl":137}],"name":"printsDetectionOutputDirs","pass":true,"statements":[{"sl":106},{"sl":107},{"sl":109},{"sl":110},{"sl":111},{"sl":112},{"sl":114},{"sl":116},{"sl":133},{"sl":134},{"sl":139},{"sl":140},{"sl":141}]},"test_625":{"methods":[{"sl":121},{"sl":131},{"sl":137}],"name":"loadsComboTechniquesFromFile","pass":true,"statements":[{"sl":123},{"sl":125},{"sl":126},{"sl":128},{"sl":133},{"sl":134},{"sl":139},{"sl":140},{"sl":141}]},"test_639":{"methods":[{"sl":97},{"sl":131},{"sl":137}],"name":"passesItselfAsPresenterToUseCase","pass":true,"statements":[{"sl":99},{"sl":100},{"sl":101},{"sl":133},{"sl":134},{"sl":139},{"sl":140},{"sl":141}]},"test_704":{"methods":[{"sl":131},{"sl":137},{"sl":144}],"name":"passesCorrectConfigurationToTemplateExclusionTechnique","pass":true,"statements":[{"sl":133},{"sl":134},{"sl":139},{"sl":140},{"sl":141},{"sl":146},{"sl":148},{"sl":149}]},"test_724":{"methods":[{"sl":89},{"sl":131},{"sl":137}],"name":"passesCorrectRequestModelToUseCase","pass":true,"statements":[{"sl":91},{"sl":93},{"sl":94},{"sl":133},{"sl":134},{"sl":139},{"sl":140},{"sl":141}]},"test_738":{"methods":[{"sl":131},{"sl":137},{"sl":152}],"name":"passesCorrectConfigurationFileToSimilarityDetectionTool","pass":true,"statements":[{"sl":133},{"sl":134},{"sl":139},{"sl":140},{"sl":141},{"sl":154},{"sl":156},{"sl":157}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [309], [], [309], [309], [], [309], [309], [], [309], [309], [], [309], [], [], [], [356], [], [356], [], [356], [], [], [724], [], [724], [], [724], [724], [], [], [639], [], [639], [639], [639], [], [], [596], [], [596], [596], [], [596], [596], [596], [596], [], [596], [], [596], [], [], [], [], [625], [], [625], [], [625], [625], [], [625], [], [], [738, 625, 639, 596, 724, 356, 704], [], [738, 625, 639, 596, 724, 356, 704], [738, 625, 639, 596, 724, 356, 704], [], [], [738, 625, 639, 596, 724, 356, 704], [], [738, 625, 639, 596, 724, 356, 704], [738, 625, 639, 596, 724, 356, 704], [738, 625, 639, 596, 724, 356, 704], [], [], [704], [], [704], [], [704], [704], [], [], [738], [], [738], [], [738], [738], [], [], [437], [437], [437], [437], [437], [437], [437], [437], [437], [437], [437], [], []]