var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":84,"id":5422,"methods":[{"el":24,"sc":5,"sl":20},{"el":28,"sc":5,"sl":26},{"el":32,"sc":5,"sl":30},{"el":36,"sc":5,"sl":34},{"el":41,"sc":5,"sl":38},{"el":57,"sc":5,"sl":43},{"el":61,"sc":5,"sl":59},{"el":65,"sc":5,"sl":63},{"el":83,"sc":5,"sl":67}],"name":"SummaryReportControler","sl":14}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_180":{"methods":[{"sl":67}],"name":"controllerPassesRequestModelToUseCaseOnUpdatePlagInfo","pass":true,"statements":[{"sl":68},{"sl":70},{"sl":71},{"sl":72},{"sl":73},{"sl":74},{"sl":77},{"sl":78},{"sl":79},{"sl":82}]},"test_194":{"methods":[{"sl":26}],"name":"beanPassesUseCaseToController","pass":true,"statements":[{"sl":27}]},"test_197":{"methods":[{"sl":63}],"name":"beanCallsLoadsProcessedToolsAndTechniques","pass":true,"statements":[{"sl":64}]},"test_39":{"methods":[{"sl":43}],"name":"requestModelIsCreatedCorrectlyOnGenerateReport","pass":true,"statements":[{"sl":44},{"sl":45},{"sl":47},{"sl":48},{"sl":49},{"sl":50},{"sl":51},{"sl":52},{"sl":53},{"sl":54},{"sl":56}]},"test_404":{"methods":[{"sl":34},{"sl":43}],"name":"presenterAndRequestModelArePassedToTheUseCaseOnGenerateReport","pass":true,"statements":[{"sl":35},{"sl":44},{"sl":45},{"sl":47},{"sl":48},{"sl":49},{"sl":50},{"sl":51},{"sl":52},{"sl":53},{"sl":54},{"sl":56}]},"test_427":{"methods":[{"sl":38},{"sl":63}],"name":"controllerClearsTechniquesNotInWorkingDir","pass":true,"statements":[{"sl":39},{"sl":40},{"sl":64}]},"test_43":{"methods":[{"sl":34}],"name":"beanPassesPresenterToController","pass":true,"statements":[{"sl":35}]},"test_444":{"methods":[{"sl":59}],"name":"controllerPassesCorrectInfoOnLoadDirList","pass":true,"statements":[{"sl":60}]},"test_487":{"methods":[{"sl":34}],"name":"presenterIsPassedToTheUseCaseOnCreationCallingGetAvailable","pass":true,"statements":[{"sl":35}]},"test_502":{"methods":[{"sl":67}],"name":"controllerPassesRequestModelToUseCaseWithNoSelectedPair","pass":true,"statements":[{"sl":68},{"sl":70},{"sl":77},{"sl":78},{"sl":79},{"sl":82}]},"test_511":{"methods":[{"sl":30}],"name":"beanPassesViewModelToController","pass":true,"statements":[{"sl":31}]},"test_514":{"methods":[{"sl":43}],"name":"controllerCanPassRequestModelWithComboTechniquesToUseCase","pass":true,"statements":[{"sl":44},{"sl":45},{"sl":47},{"sl":48},{"sl":49},{"sl":50},{"sl":51},{"sl":52},{"sl":53},{"sl":54},{"sl":55},{"sl":56}]},"test_566":{"methods":[{"sl":20},{"sl":38},{"sl":43}],"name":"controllerCanPassRequestModelWithNoTechnique","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":39},{"sl":40},{"sl":44},{"sl":45},{"sl":47},{"sl":48},{"sl":49},{"sl":50},{"sl":51},{"sl":52},{"sl":54},{"sl":56}]},"test_7":{"methods":[{"sl":20},{"sl":38},{"sl":59},{"sl":63}],"name":"everyBeanHasItsOwnInstanceOfFactorieProvider","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":39},{"sl":40},{"sl":60},{"sl":64}]},"test_8":{"methods":[{"sl":43}],"name":"controllerCanPassRequestModelWithNoComboTechnique","pass":true,"statements":[{"sl":44},{"sl":45},{"sl":47},{"sl":48},{"sl":49},{"sl":50},{"sl":51},{"sl":52},{"sl":53},{"sl":54},{"sl":56}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [566, 7], [566, 7], [566, 7], [566, 7], [], [], [194], [194], [], [], [511], [511], [], [], [43, 404, 487], [43, 404, 487], [], [], [566, 7, 427], [566, 7, 427], [566, 7, 427], [], [], [39, 566, 404, 8, 514], [39, 566, 404, 8, 514], [39, 566, 404, 8, 514], [], [39, 566, 404, 8, 514], [39, 566, 404, 8, 514], [39, 566, 404, 8, 514], [39, 566, 404, 8, 514], [39, 566, 404, 8, 514], [39, 566, 404, 8, 514], [39, 404, 8, 514], [39, 566, 404, 8, 514], [514], [39, 566, 404, 8, 514], [], [], [7, 444], [7, 444], [], [], [7, 197, 427], [7, 197, 427], [], [], [502, 180], [502, 180], [], [502, 180], [180], [180], [180], [180], [], [], [502, 180], [502, 180], [502, 180], [], [], [502, 180], [], []]
