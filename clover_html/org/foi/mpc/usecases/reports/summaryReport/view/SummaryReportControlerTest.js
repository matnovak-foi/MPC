var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":233,"id":10601,"methods":[{"el":55,"sc":5,"sl":35},{"el":64,"sc":5,"sl":57},{"el":70,"sc":5,"sl":66},{"el":84,"sc":5,"sl":72},{"el":96,"sc":5,"sl":86},{"el":104,"sc":5,"sl":98},{"el":119,"sc":5,"sl":106},{"el":131,"sc":5,"sl":121},{"el":155,"sc":5,"sl":133},{"el":177,"sc":5,"sl":157},{"el":193,"sc":5,"sl":179}],"name":"SummaryReportControlerTest","sl":25},{"el":232,"id":10704,"methods":[{"el":207,"sc":9,"sl":203},{"el":213,"sc":9,"sl":209},{"el":220,"sc":9,"sl":215},{"el":226,"sc":9,"sl":222},{"el":231,"sc":9,"sl":228}],"name":"SummaryReportControlerTest.SummaryReportInputBoundarySpy","sl":195}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_180":{"methods":[{"sl":157},{"sl":222}],"name":"controllerPassesRequestModelToUseCaseOnUpdatePlagInfo","pass":true,"statements":[{"sl":159},{"sl":160},{"sl":161},{"sl":162},{"sl":163},{"sl":164},{"sl":166},{"sl":168},{"sl":169},{"sl":170},{"sl":171},{"sl":172},{"sl":173},{"sl":174},{"sl":175},{"sl":176},{"sl":224},{"sl":225}]},"test_39":{"methods":[{"sl":72},{"sl":209}],"name":"requestModelIsCreatedCorrectlyOnGenerateReport","pass":true,"statements":[{"sl":74},{"sl":76},{"sl":77},{"sl":78},{"sl":79},{"sl":80},{"sl":81},{"sl":82},{"sl":83},{"sl":211},{"sl":212}]},"test_404":{"methods":[{"sl":57},{"sl":209}],"name":"presenterAndRequestModelArePassedToTheUseCaseOnGenerateReport","pass":true,"statements":[{"sl":59},{"sl":61},{"sl":62},{"sl":63},{"sl":211},{"sl":212}]},"test_427":{"methods":[{"sl":133}],"name":"controllerClearsTechniquesNotInWorkingDir","pass":true,"statements":[{"sl":135},{"sl":136},{"sl":137},{"sl":138},{"sl":140},{"sl":141},{"sl":142},{"sl":144},{"sl":145},{"sl":146},{"sl":148},{"sl":149},{"sl":151},{"sl":153},{"sl":154}]},"test_444":{"methods":[{"sl":121},{"sl":215}],"name":"controllerPassesCorrectInfoOnLoadDirList","pass":true,"statements":[{"sl":123},{"sl":124},{"sl":126},{"sl":128},{"sl":129},{"sl":130},{"sl":217},{"sl":218},{"sl":219}]},"test_487":{"methods":[{"sl":66}],"name":"presenterIsPassedToTheUseCaseOnCreationCallingGetAvailable","pass":true,"statements":[{"sl":68},{"sl":69}]},"test_502":{"methods":[{"sl":179},{"sl":222}],"name":"controllerPassesRequestModelToUseCaseWithNoSelectedPair","pass":true,"statements":[{"sl":181},{"sl":183},{"sl":184},{"sl":186},{"sl":187},{"sl":188},{"sl":189},{"sl":190},{"sl":191},{"sl":192},{"sl":224},{"sl":225}]},"test_514":{"methods":[{"sl":106},{"sl":209}],"name":"controllerCanPassRequestModelWithComboTechniquesToUseCase","pass":true,"statements":[{"sl":108},{"sl":111},{"sl":113},{"sl":115},{"sl":116},{"sl":117},{"sl":118},{"sl":211},{"sl":212}]},"test_566":{"methods":[{"sl":86},{"sl":203},{"sl":209}],"name":"controllerCanPassRequestModelWithNoTechnique","pass":true,"statements":[{"sl":88},{"sl":90},{"sl":91},{"sl":92},{"sl":94},{"sl":95},{"sl":205},{"sl":206},{"sl":211},{"sl":212}]},"test_8":{"methods":[{"sl":98},{"sl":209}],"name":"controllerCanPassRequestModelWithNoComboTechnique","pass":true,"statements":[{"sl":100},{"sl":102},{"sl":103},{"sl":211},{"sl":212}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [404], [], [404], [], [404], [404], [404], [], [], [487], [], [487], [487], [], [], [39], [], [39], [], [39], [39], [39], [39], [39], [39], [39], [39], [], [], [566], [], [566], [], [566], [566], [566], [], [566], [566], [], [], [8], [], [8], [], [8], [8], [], [], [514], [], [514], [], [], [514], [], [514], [], [514], [514], [514], [514], [], [], [444], [], [444], [444], [], [444], [], [444], [444], [444], [], [], [427], [], [427], [427], [427], [427], [], [427], [427], [427], [], [427], [427], [427], [], [427], [427], [], [427], [], [427], [427], [], [], [180], [], [180], [180], [180], [180], [180], [180], [], [180], [], [180], [180], [180], [180], [180], [180], [180], [180], [180], [], [], [502], [], [502], [], [502], [502], [], [502], [502], [502], [502], [502], [502], [502], [], [], [], [], [], [], [], [], [], [], [566], [], [566], [566], [], [], [8, 39, 566, 514, 404], [], [8, 39, 566, 514, 404], [8, 39, 566, 514, 404], [], [], [444], [], [444], [444], [444], [], [], [502, 180], [], [502, 180], [502, 180], [], [], [], [], [], [], [], []]
