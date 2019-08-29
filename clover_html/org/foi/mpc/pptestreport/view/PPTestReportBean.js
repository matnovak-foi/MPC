var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":99,"id":13400,"methods":[{"el":57,"sc":5,"sl":33},{"el":61,"sc":5,"sl":59},{"el":65,"sc":5,"sl":63},{"el":67,"sc":5,"sl":67},{"el":71,"sc":5,"sl":69},{"el":75,"sc":5,"sl":73},{"el":79,"sc":5,"sl":77},{"el":83,"sc":5,"sl":81},{"el":87,"sc":5,"sl":85},{"el":91,"sc":5,"sl":89},{"el":98,"sc":5,"sl":93}],"name":"PPTestReportBean","sl":21}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_143":{"methods":[{"sl":69},{"sl":77}],"name":"beanCreatesUseCase","pass":true,"statements":[{"sl":70},{"sl":78}]},"test_314":{"methods":[{"sl":33},{"sl":69}],"name":"everyBeanHasItsOwnInstanceOfFactorieProvider","pass":true,"statements":[{"sl":34},{"sl":35},{"sl":36},{"sl":37},{"sl":39},{"sl":40},{"sl":42},{"sl":43},{"sl":44},{"sl":45},{"sl":47},{"sl":49},{"sl":50},{"sl":52},{"sl":53},{"sl":54},{"sl":56},{"sl":70}]},"test_351":{"methods":[{"sl":59}],"name":"PPreportForTestPairWithJPlag","pass":true,"statements":[{"sl":60}]},"test_368":{"methods":[{"sl":63}],"name":"beanCreatesViewModel","pass":true,"statements":[{"sl":64}]},"test_372":{"methods":[{"sl":69}],"name":"passesCorrectConfigurationToTemplateExclusionTechnique","pass":true,"statements":[{"sl":70}]},"test_381":{"methods":[{"sl":59},{"sl":63}],"name":"beanPassesViewModelToController","pass":true,"statements":[{"sl":60},{"sl":64}]},"test_419":{"methods":[{"sl":59},{"sl":67}],"name":"beanPassesPresenterToController","pass":true,"statements":[{"sl":60},{"sl":67}]},"test_441":{"methods":[{"sl":59},{"sl":81}],"name":"runDetectionWithComboTechnique","pass":true,"statements":[{"sl":60},{"sl":82}]},"test_490":{"methods":[{"sl":69}],"name":"passesCorrectConfigurationFileToSimilarityDetectionTool","pass":true,"statements":[{"sl":70}]},"test_501":{"methods":[{"sl":77}],"name":"loadsComboTechniquesFromFile","pass":true,"statements":[{"sl":78}]},"test_534":{"methods":[{"sl":59}],"name":"PPreportForTestPairCanBeRunTwice","pass":true,"statements":[{"sl":60}]},"test_563":{"methods":[{"sl":59},{"sl":69},{"sl":77},{"sl":81}],"name":"beanPassesUseCasesToControllers","pass":true,"statements":[{"sl":60},{"sl":70},{"sl":78},{"sl":82}]},"test_608":{"methods":[{"sl":67}],"name":"beanCreatesPresenter","pass":true,"statements":[{"sl":67}]},"test_702":{"methods":[{"sl":59}],"name":"beanCreatesController","pass":true,"statements":[{"sl":60}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [314], [314], [314], [314], [314], [], [314], [314], [], [314], [314], [314], [314], [], [314], [], [314], [314], [], [314], [314], [314], [], [314], [], [], [441, 419, 702, 351, 534, 563, 381], [441, 419, 702, 351, 534, 563, 381], [], [], [368, 381], [368, 381], [], [], [419, 608], [], [490, 314, 372, 563, 143], [490, 314, 372, 563, 143], [], [], [], [], [], [], [501, 563, 143], [501, 563, 143], [], [], [441, 563], [441, 563], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []]