var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":124,"id":2958,"methods":[{"el":30,"sc":5,"sl":27},{"el":36,"sc":5,"sl":32},{"el":60,"sc":5,"sl":38},{"el":64,"sc":5,"sl":62},{"el":72,"sc":5,"sl":66},{"el":76,"sc":5,"sl":74},{"el":80,"sc":5,"sl":78},{"el":92,"sc":5,"sl":82},{"el":96,"sc":5,"sl":94},{"el":100,"sc":5,"sl":98}],"name":"MultipleDetectionMain","sl":20},{"el":123,"id":3008,"methods":[{"el":107,"sc":9,"sl":104},{"el":112,"sc":9,"sl":109},{"el":117,"sc":9,"sl":114},{"el":122,"sc":9,"sl":119}],"name":"MultipleDetectionMain.ComboTechniquePresenterDummy","sl":102}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_309":{"methods":[{"sl":38}],"name":"ifWrongParamsAreGivenReturnError","pass":true,"statements":[{"sl":39},{"sl":40},{"sl":41},{"sl":42},{"sl":44},{"sl":45}]},"test_356":{"methods":[{"sl":38},{"sl":66},{"sl":78},{"sl":82},{"sl":109}],"name":"canReadPropertiesFile","pass":true,"statements":[{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":79},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91}]},"test_437":{"methods":[{"sl":27},{"sl":32},{"sl":38},{"sl":66},{"sl":82},{"sl":109}],"name":"canRunMultipleDetectionOnStudentsDirs","pass":true,"statements":[{"sl":28},{"sl":29},{"sl":33},{"sl":34},{"sl":35},{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":71},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91}]},"test_596":{"methods":[{"sl":38},{"sl":66},{"sl":82},{"sl":109}],"name":"printsDetectionOutputDirs","pass":true,"statements":[{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":71},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91}]},"test_625":{"methods":[{"sl":38},{"sl":66},{"sl":82},{"sl":94},{"sl":109}],"name":"loadsComboTechniquesFromFile","pass":true,"statements":[{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91},{"sl":95}]},"test_639":{"methods":[{"sl":38},{"sl":66},{"sl":82},{"sl":109}],"name":"passesItselfAsPresenterToUseCase","pass":true,"statements":[{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91}]},"test_704":{"methods":[{"sl":38},{"sl":66},{"sl":82},{"sl":109}],"name":"passesCorrectConfigurationToTemplateExclusionTechnique","pass":true,"statements":[{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91}]},"test_724":{"methods":[{"sl":38},{"sl":66},{"sl":82},{"sl":109}],"name":"passesCorrectRequestModelToUseCase","pass":true,"statements":[{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91}]},"test_738":{"methods":[{"sl":38},{"sl":66},{"sl":82},{"sl":109}],"name":"passesCorrectConfigurationFileToSimilarityDetectionTool","pass":true,"statements":[{"sl":39},{"sl":48},{"sl":49},{"sl":51},{"sl":52},{"sl":54},{"sl":55},{"sl":57},{"sl":58},{"sl":59},{"sl":68},{"sl":69},{"sl":70},{"sl":83},{"sl":85},{"sl":86},{"sl":87},{"sl":88},{"sl":89},{"sl":91}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [437], [437], [437], [], [], [437], [437], [437], [437], [], [], [738, 437, 704, 639, 596, 356, 724, 309, 625], [738, 437, 704, 639, 596, 356, 724, 309, 625], [309], [309], [309], [], [309], [309], [], [], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [], [], [], [], [], [], [738, 437, 704, 639, 596, 356, 724, 625], [], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [437, 596], [], [], [], [], [], [], [356], [356], [], [], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [738, 437, 704, 639, 596, 356, 724, 625], [], [738, 437, 704, 639, 596, 356, 724, 625], [], [], [625], [625], [], [], [], [], [], [], [], [], [], [], [], [], [], [738, 437, 704, 639, 596, 356, 724, 625], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []]