var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":307,"id":8616,"methods":[{"el":56,"sc":5,"sl":44},{"el":63,"sc":5,"sl":58},{"el":81,"sc":5,"sl":65},{"el":86,"sc":5,"sl":83},{"el":93,"sc":5,"sl":88},{"el":100,"sc":5,"sl":95},{"el":107,"sc":5,"sl":102},{"el":114,"sc":5,"sl":109},{"el":137,"sc":5,"sl":116},{"el":147,"sc":5,"sl":139},{"el":158,"sc":5,"sl":149},{"el":173,"sc":5,"sl":160},{"el":190,"sc":5,"sl":175},{"el":207,"sc":5,"sl":192},{"el":222,"sc":5,"sl":209},{"el":234,"sc":5,"sl":224},{"el":244,"sc":5,"sl":236},{"el":255,"sc":5,"sl":246},{"el":301,"sc":5,"sl":298},{"el":306,"sc":5,"sl":303}],"name":"ToolCalibratorUseCaseTest","sl":35},{"el":296,"id":8756,"methods":[{"el":288,"sc":17,"sl":267},{"el":293,"sc":17,"sl":290},{"el":295,"sc":9,"sl":264}],"name":"ToolCalibratorUseCaseTest.SimilarityDetectionToolCalibratorFactorySpy","sl":257}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_203":{"methods":[{"sl":224},{"sl":264},{"sl":267},{"sl":290},{"sl":298},{"sl":303}],"name":"generatesCorrectOptimalToolParams","pass":true,"statements":[{"sl":226},{"sl":228},{"sl":230},{"sl":231},{"sl":232},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":300},{"sl":305}]},"test_224":{"methods":[{"sl":175},{"sl":264},{"sl":267},{"sl":290},{"sl":298},{"sl":303}],"name":"generatesCorrectBaseToolParamsForSIM","pass":true,"statements":[{"sl":177},{"sl":178},{"sl":179},{"sl":180},{"sl":181},{"sl":183},{"sl":185},{"sl":186},{"sl":187},{"sl":188},{"sl":189},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":300},{"sl":305}]},"test_288":{"methods":[{"sl":109},{"sl":303}],"name":"givenNoWorkingDirReturnError","pass":true,"statements":[{"sl":111},{"sl":112},{"sl":113},{"sl":305}]},"test_379":{"methods":[{"sl":139},{"sl":303}],"name":"givenWrongBaseToolParamJPlag","pass":true,"statements":[{"sl":141},{"sl":142},{"sl":143},{"sl":144},{"sl":145},{"sl":146},{"sl":305}]},"test_428":{"methods":[{"sl":192},{"sl":264},{"sl":267},{"sl":290},{"sl":303}],"name":"generatesCorrectBaseToolParamsIfMatchIsZero","pass":true,"statements":[{"sl":194},{"sl":195},{"sl":196},{"sl":197},{"sl":199},{"sl":201},{"sl":202},{"sl":203},{"sl":204},{"sl":205},{"sl":206},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":305}]},"test_449":{"methods":[{"sl":209},{"sl":264},{"sl":267},{"sl":290},{"sl":298},{"sl":303}],"name":"generatesCorrectCalibratedToolParams","pass":true,"statements":[{"sl":211},{"sl":213},{"sl":215},{"sl":216},{"sl":217},{"sl":218},{"sl":219},{"sl":220},{"sl":221},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":300},{"sl":305}]},"test_481":{"methods":[{"sl":95},{"sl":303}],"name":"givenNoToCalibrateToolReturnError","pass":true,"statements":[{"sl":97},{"sl":98},{"sl":99},{"sl":305}]},"test_547":{"methods":[{"sl":246},{"sl":264},{"sl":267},{"sl":290},{"sl":298},{"sl":303}],"name":"similarityDetectionToolCalibratorWasCalled","pass":true,"statements":[{"sl":248},{"sl":249},{"sl":250},{"sl":252},{"sl":253},{"sl":254},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":300},{"sl":305}]},"test_622":{"methods":[{"sl":116},{"sl":264},{"sl":267},{"sl":290},{"sl":298},{"sl":303}],"name":"createCorrectPhaseRunnerForBaseTool","pass":true,"statements":[{"sl":118},{"sl":120},{"sl":121},{"sl":122},{"sl":124},{"sl":125},{"sl":126},{"sl":127},{"sl":129},{"sl":130},{"sl":132},{"sl":133},{"sl":134},{"sl":135},{"sl":136},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":300},{"sl":305}]},"test_701":{"methods":[{"sl":149},{"sl":303}],"name":"givenWrongBaseToolParamSIM","pass":true,"statements":[{"sl":151},{"sl":152},{"sl":153},{"sl":154},{"sl":155},{"sl":156},{"sl":157},{"sl":305}]},"test_730":{"methods":[{"sl":88},{"sl":303}],"name":"givenNoBaseToolReturnError","pass":true,"statements":[{"sl":90},{"sl":91},{"sl":92},{"sl":305}]},"test_753":{"methods":[{"sl":160},{"sl":264},{"sl":267},{"sl":290},{"sl":298},{"sl":303}],"name":"generatesCorrectBaseToolParamsForJPlag","pass":true,"statements":[{"sl":162},{"sl":163},{"sl":164},{"sl":166},{"sl":168},{"sl":169},{"sl":170},{"sl":171},{"sl":172},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":300},{"sl":305}]},"test_758":{"methods":[{"sl":236},{"sl":264},{"sl":267},{"sl":290},{"sl":298},{"sl":303}],"name":"generatesCorrectAllComboParamsDiff","pass":true,"statements":[{"sl":238},{"sl":240},{"sl":242},{"sl":243},{"sl":266},{"sl":269},{"sl":270},{"sl":271},{"sl":272},{"sl":273},{"sl":274},{"sl":275},{"sl":276},{"sl":277},{"sl":278},{"sl":279},{"sl":280},{"sl":281},{"sl":282},{"sl":283},{"sl":284},{"sl":285},{"sl":286},{"sl":287},{"sl":292},{"sl":300},{"sl":305}]},"test_765":{"methods":[{"sl":102},{"sl":303}],"name":"givenNoSourceDirReturnError","pass":true,"statements":[{"sl":104},{"sl":105},{"sl":106},{"sl":305}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [730], [], [730], [730], [730], [], [], [481], [], [481], [481], [481], [], [], [765], [], [765], [765], [765], [], [], [288], [], [288], [288], [288], [], [], [622], [], [622], [], [622], [622], [622], [], [622], [622], [622], [622], [], [622], [622], [], [622], [622], [622], [622], [622], [], [], [379], [], [379], [379], [379], [379], [379], [379], [], [], [701], [], [701], [701], [701], [701], [701], [701], [701], [], [], [753], [], [753], [753], [753], [], [753], [], [753], [753], [753], [753], [753], [], [], [224], [], [224], [224], [224], [224], [224], [], [224], [], [224], [224], [224], [224], [224], [], [], [428], [], [428], [428], [428], [428], [], [428], [], [428], [428], [428], [428], [428], [428], [], [], [449], [], [449], [], [449], [], [449], [449], [449], [449], [449], [449], [449], [], [], [203], [], [203], [], [203], [], [203], [203], [203], [], [], [], [758], [], [758], [], [758], [], [758], [758], [], [], [547], [], [547], [547], [547], [], [547], [547], [547], [], [], [], [], [], [], [], [], [], [547, 753, 224, 203, 428, 622, 449, 758], [], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [547, 753, 224, 203, 428, 622, 449, 758], [], [], [547, 753, 224, 203, 428, 622, 449, 758], [], [547, 753, 224, 203, 428, 622, 449, 758], [], [], [], [], [], [547, 753, 224, 203, 622, 449, 758], [], [547, 753, 224, 203, 622, 449, 758], [], [], [547, 753, 288, 481, 224, 765, 203, 379, 428, 701, 730, 622, 449, 758], [], [547, 753, 288, 481, 224, 765, 203, 379, 428, 701, 730, 622, 449, 758], [], []]