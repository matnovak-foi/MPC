var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":75,"id":3478,"methods":[{"el":37,"sc":5,"sl":26},{"el":43,"sc":5,"sl":41},{"el":49,"sc":5,"sl":45},{"el":65,"sc":5,"sl":51},{"el":70,"sc":5,"sl":67},{"el":74,"sc":5,"sl":72}],"name":"SimilarityDetectionToolFactory","sl":19}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_1":{"methods":[{"sl":41}],"name":"isPPTestReportInputBoudary","pass":true,"statements":[{"sl":42}]},"test_149":{"methods":[{"sl":51}],"name":"throwExceptionIfToolNameIsEmpty","pass":false,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":62},{"sl":63}]},"test_158":{"methods":[{"sl":51}],"name":"responseIsOkWhenInputIsOk","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_16":{"methods":[{"sl":45},{"sl":51}],"name":"createsJPlagTextWithConfigFromFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48},{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_203":{"methods":[{"sl":51}],"name":"generatesCorrectOptimalToolParams","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_224":{"methods":[{"sl":51}],"name":"generatesCorrectBaseToolParamsForSIM","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_232":{"methods":[{"sl":45},{"sl":51}],"name":"createsSIMJavaWithConfigFromFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48},{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_269":{"methods":[{"sl":45},{"sl":51}],"name":"createsSherlockJavaWithConfigFromFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48},{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_279":{"methods":[{"sl":41},{"sl":67}],"name":"useCaseReturnsAvailableToolsOnGetAvailableTools","pass":true,"statements":[{"sl":42},{"sl":69}]},"test_314":{"methods":[{"sl":41},{"sl":45},{"sl":67}],"name":"everyBeanHasItsOwnInstanceOfFactorieProvider","pass":true,"statements":[{"sl":42},{"sl":46},{"sl":47},{"sl":48},{"sl":69}]},"test_34":{"methods":[{"sl":41}],"name":"ifWrongParamsAreGivenReturnError","pass":true,"statements":[{"sl":42}]},"test_351":{"methods":[{"sl":51}],"name":"PPreportForTestPairWithJPlag","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_356":{"methods":[{"sl":45}],"name":"canReadPropertiesFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48}]},"test_379":{"methods":[{"sl":51}],"name":"givenWrongBaseToolParamJPlag","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_383":{"methods":[{"sl":41}],"name":"isSummaryReportInputBoudary","pass":true,"statements":[{"sl":42}]},"test_405":{"methods":[{"sl":41}],"name":"useCaseRetrunsListOfDirsForGivenInputDir","pass":true,"statements":[{"sl":42}]},"test_407":{"methods":[{"sl":45},{"sl":51}],"name":"createsSherlockTextWithConfigFromFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48},{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_416":{"methods":[{"sl":41}],"name":"useCaseCallsDirectoryPreparerWithCorrectInputDirDepthAndDir","pass":true,"statements":[{"sl":42}]},"test_417":{"methods":[{"sl":45},{"sl":51}],"name":"createsJPlagJavaWithConfigFromFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48},{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_427":{"methods":[{"sl":41},{"sl":67}],"name":"controllerClearsTechniquesNotInWorkingDir","pass":true,"statements":[{"sl":42},{"sl":69}]},"test_428":{"methods":[{"sl":51}],"name":"generatesCorrectBaseToolParamsIfMatchIsZero","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_437":{"methods":[{"sl":41},{"sl":45},{"sl":51}],"name":"canRunMultipleDetectionOnStudentsDirs","pass":true,"statements":[{"sl":42},{"sl":46},{"sl":47},{"sl":48},{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_441":{"methods":[{"sl":51}],"name":"runDetectionWithComboTechnique","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_449":{"methods":[{"sl":51}],"name":"generatesCorrectCalibratedToolParams","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_453":{"methods":[{"sl":41},{"sl":67}],"name":"translatesResponseModelOfAvalibleToolsToViewModel","pass":true,"statements":[{"sl":42},{"sl":69}]},"test_490":{"methods":[{"sl":72}],"name":"passesCorrectConfigurationFileToSimilarityDetectionTool","pass":true,"statements":[{"sl":73}]},"test_497":{"methods":[{"sl":41},{"sl":67}],"name":"translatesResponseModelOfAvalibleToolsToViewModel","pass":true,"statements":[{"sl":42},{"sl":69}]},"test_503":{"methods":[{"sl":51}],"name":"createsCorrectPhaseRunner","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_513":{"methods":[{"sl":51}],"name":"enablesCorrectPhasesToRun","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_534":{"methods":[{"sl":51}],"name":"PPreportForTestPairCanBeRunTwice","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_546":{"methods":[{"sl":51}],"name":"throwExceptionIfToolNameIsWrongWhenCreating","pass":false,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":62},{"sl":63}]},"test_547":{"methods":[{"sl":51}],"name":"similarityDetectionToolCalibratorWasCalled","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_572":{"methods":[{"sl":41},{"sl":67}],"name":"useCaseReturnsAvailableToolsOnGetAvailableTools","pass":true,"statements":[{"sl":42},{"sl":69}]},"test_596":{"methods":[{"sl":45}],"name":"printsDetectionOutputDirs","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48}]},"test_622":{"methods":[{"sl":51}],"name":"createCorrectPhaseRunnerForBaseTool","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_625":{"methods":[{"sl":45}],"name":"loadsComboTechniquesFromFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48}]},"test_636":{"methods":[{"sl":51}],"name":"canRunAllToolsOnAllTehcniques","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_639":{"methods":[{"sl":45}],"name":"passesItselfAsPresenterToUseCase","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48}]},"test_642":{"methods":[{"sl":51}],"name":"calibrationSIMTestWorksCase1","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_663":{"methods":[{"sl":51}],"name":"createsToolObjectBasedOnName","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_679":{"methods":[{"sl":51}],"name":"calibrationSIMTestSelfWorksCase","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_695":{"methods":[{"sl":41}],"name":"canRunMultipleCalibrations","pass":true,"statements":[{"sl":42}]},"test_699":{"methods":[{"sl":45},{"sl":51}],"name":"createsSIMTextWithConfigFromFile","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48},{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":59},{"sl":60}]},"test_7":{"methods":[{"sl":41},{"sl":45},{"sl":67}],"name":"everyBeanHasItsOwnInstanceOfFactorieProvider","pass":true,"statements":[{"sl":42},{"sl":46},{"sl":47},{"sl":48},{"sl":69}]},"test_701":{"methods":[{"sl":51}],"name":"givenWrongBaseToolParamSIM","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_704":{"methods":[{"sl":45}],"name":"passesCorrectConfigurationToTemplateExclusionTechnique","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48}]},"test_714":{"methods":[{"sl":72}],"name":"passesCorrectConfigurationFileToSimilarityDetectionTool","pass":true,"statements":[{"sl":73}]},"test_718":{"methods":[{"sl":51}],"name":"calibrationSIMTestWorksCase2","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_724":{"methods":[{"sl":45}],"name":"passesCorrectRequestModelToUseCase","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48}]},"test_733":{"methods":[{"sl":41},{"sl":67}],"name":"translatesResponseModelOfAvalibleToolsToViewModel","pass":true,"statements":[{"sl":42},{"sl":69}]},"test_738":{"methods":[{"sl":45},{"sl":72}],"name":"passesCorrectConfigurationFileToSimilarityDetectionTool","pass":true,"statements":[{"sl":46},{"sl":47},{"sl":48},{"sl":73}]},"test_753":{"methods":[{"sl":51}],"name":"generatesCorrectBaseToolParamsForJPlag","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]},"test_754":{"methods":[{"sl":67}],"name":"factoryReturnsAvailableTools","pass":true,"statements":[{"sl":69}]},"test_758":{"methods":[{"sl":51}],"name":"generatesCorrectAllComboParamsDiff","pass":true,"statements":[{"sl":53},{"sl":55},{"sl":56},{"sl":57},{"sl":58},{"sl":60}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [383, 497, 437, 416, 405, 427, 279, 695, 314, 7, 453, 572, 34, 1, 733], [383, 497, 437, 416, 405, 427, 279, 695, 314, 7, 453, 572, 34, 1, 733], [], [], [738, 437, 699, 704, 639, 596, 269, 356, 314, 7, 417, 16, 407, 724, 625, 232], [738, 437, 699, 704, 639, 596, 269, 356, 314, 7, 417, 16, 407, 724, 625, 232], [738, 437, 699, 704, 639, 596, 269, 356, 314, 7, 417, 16, 407, 724, 625, 232], [738, 437, 699, 704, 639, 596, 269, 356, 314, 7, 417, 16, 407, 724, 625, 232], [], [], [224, 428, 701, 437, 449, 699, 503, 753, 718, 149, 379, 642, 546, 636, 269, 758, 547, 679, 203, 158, 417, 622, 351, 513, 534, 441, 16, 407, 232, 663], [], [224, 428, 701, 437, 449, 699, 503, 753, 718, 149, 379, 642, 546, 636, 269, 758, 547, 679, 203, 158, 417, 622, 351, 513, 534, 441, 16, 407, 232, 663], [], [224, 428, 701, 437, 449, 699, 503, 753, 718, 149, 379, 642, 546, 636, 269, 758, 547, 679, 203, 158, 417, 622, 351, 513, 534, 441, 16, 407, 232, 663], [224, 428, 701, 437, 449, 699, 503, 753, 718, 149, 379, 642, 546, 636, 269, 758, 547, 679, 203, 158, 417, 622, 351, 513, 534, 441, 16, 407, 232, 663], [224, 428, 701, 437, 449, 699, 503, 753, 718, 379, 642, 636, 269, 758, 547, 679, 203, 158, 417, 622, 351, 513, 534, 441, 16, 407, 232, 663], [224, 428, 701, 437, 449, 699, 503, 753, 718, 379, 642, 636, 269, 758, 547, 679, 203, 158, 417, 622, 351, 513, 534, 441, 16, 407, 232, 663], [437, 699, 269, 417, 351, 534, 441, 16, 407, 232], [224, 428, 701, 437, 449, 699, 503, 753, 718, 379, 642, 636, 269, 758, 547, 679, 203, 158, 417, 622, 351, 513, 534, 441, 16, 407, 232, 663], [], [149, 546], [149, 546], [], [], [], [497, 754, 427, 279, 314, 7, 453, 572, 733], [], [497, 754, 427, 279, 314, 7, 453, 572, 733], [], [], [714, 738, 490], [714, 738, 490], [], []]
