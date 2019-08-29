var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":114,"id":5089,"methods":[{"el":26,"sc":5,"sl":24},{"el":40,"sc":5,"sl":28},{"el":46,"sc":5,"sl":42},{"el":65,"sc":5,"sl":48},{"el":70,"sc":5,"sl":67},{"el":79,"sc":5,"sl":72},{"el":103,"sc":5,"sl":81},{"el":108,"sc":5,"sl":106},{"el":113,"sc":5,"sl":110}],"name":"MultipleDetectionUseCase","sl":17}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_105":{"methods":[{"sl":28},{"sl":42},{"sl":81}],"name":"withInvalidDepthNumber","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":33},{"sl":34},{"sl":43},{"sl":44},{"sl":45},{"sl":82},{"sl":86},{"sl":90},{"sl":94},{"sl":98},{"sl":99},{"sl":100}]},"test_127":{"methods":[{"sl":67}],"name":"worksWithDepth3","pass":true,"statements":[{"sl":69}]},"test_158":{"methods":[{"sl":28},{"sl":42},{"sl":48},{"sl":72},{"sl":81}],"name":"responseIsOkWhenInputIsOk","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":37},{"sl":39},{"sl":43},{"sl":44},{"sl":45},{"sl":49},{"sl":50},{"sl":51},{"sl":53},{"sl":62},{"sl":64},{"sl":73},{"sl":74},{"sl":75},{"sl":76},{"sl":78},{"sl":82},{"sl":86},{"sl":90},{"sl":94},{"sl":98},{"sl":102}]},"test_222":{"methods":[{"sl":67}],"name":"worksWithDepth2","pass":true,"statements":[{"sl":69}]},"test_255":{"methods":[{"sl":28},{"sl":42},{"sl":81}],"name":"withNoTechniqueSelected","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":33},{"sl":34},{"sl":43},{"sl":44},{"sl":45},{"sl":82},{"sl":86},{"sl":90},{"sl":94},{"sl":95},{"sl":96}]},"test_391":{"methods":[{"sl":28},{"sl":42},{"sl":81}],"name":"givenInvalidSourceDirWriteResponse","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":33},{"sl":34},{"sl":43},{"sl":44},{"sl":45},{"sl":82},{"sl":86},{"sl":87},{"sl":88}]},"test_437":{"methods":[{"sl":24},{"sl":28},{"sl":42},{"sl":48},{"sl":72},{"sl":81},{"sl":110}],"name":"canRunMultipleDetectionOnStudentsDirs","pass":true,"statements":[{"sl":25},{"sl":30},{"sl":32},{"sl":37},{"sl":39},{"sl":43},{"sl":44},{"sl":45},{"sl":49},{"sl":50},{"sl":51},{"sl":53},{"sl":62},{"sl":64},{"sl":73},{"sl":74},{"sl":75},{"sl":76},{"sl":78},{"sl":82},{"sl":86},{"sl":90},{"sl":94},{"sl":98},{"sl":102},{"sl":111},{"sl":112}]},"test_5":{"methods":[{"sl":67}],"name":"worksWithDepth1","pass":true,"statements":[{"sl":69}]},"test_503":{"methods":[{"sl":28},{"sl":42},{"sl":48},{"sl":72},{"sl":81}],"name":"createsCorrectPhaseRunner","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":37},{"sl":39},{"sl":43},{"sl":44},{"sl":45},{"sl":49},{"sl":50},{"sl":51},{"sl":53},{"sl":62},{"sl":64},{"sl":73},{"sl":74},{"sl":75},{"sl":76},{"sl":78},{"sl":82},{"sl":86},{"sl":90},{"sl":94},{"sl":98},{"sl":102}]},"test_513":{"methods":[{"sl":28},{"sl":42},{"sl":48},{"sl":72},{"sl":81}],"name":"enablesCorrectPhasesToRun","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":37},{"sl":39},{"sl":43},{"sl":44},{"sl":45},{"sl":49},{"sl":50},{"sl":51},{"sl":53},{"sl":62},{"sl":64},{"sl":73},{"sl":74},{"sl":75},{"sl":76},{"sl":78},{"sl":82},{"sl":86},{"sl":90},{"sl":94},{"sl":98},{"sl":102}]},"test_630":{"methods":[{"sl":28},{"sl":42},{"sl":81}],"name":"withNoToolSelected","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":33},{"sl":34},{"sl":43},{"sl":44},{"sl":45},{"sl":82},{"sl":86},{"sl":90},{"sl":91},{"sl":92}]},"test_697":{"methods":[{"sl":67}],"name":"worksWithDepth0","pass":true,"statements":[{"sl":69}]},"test_89":{"methods":[{"sl":28},{"sl":42},{"sl":81}],"name":"givenInvalidWorkingDirWriteResponse","pass":true,"statements":[{"sl":30},{"sl":32},{"sl":33},{"sl":34},{"sl":43},{"sl":44},{"sl":45},{"sl":82},{"sl":83},{"sl":84}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [437], [437], [], [], [255, 158, 437, 513, 391, 503, 630, 105, 89], [], [255, 158, 437, 513, 391, 503, 630, 105, 89], [], [255, 158, 437, 513, 391, 503, 630, 105, 89], [255, 391, 630, 105, 89], [255, 391, 630, 105, 89], [], [], [158, 437, 513, 503], [], [158, 437, 513, 503], [], [], [255, 158, 437, 513, 391, 503, 630, 105, 89], [255, 158, 437, 513, 391, 503, 630, 105, 89], [255, 158, 437, 513, 391, 503, 630, 105, 89], [255, 158, 437, 513, 391, 503, 630, 105, 89], [], [], [158, 437, 513, 503], [158, 437, 513, 503], [158, 437, 513, 503], [158, 437, 513, 503], [], [158, 437, 513, 503], [], [], [], [], [], [], [], [], [158, 437, 513, 503], [], [158, 437, 513, 503], [], [], [127, 5, 697, 222], [], [127, 5, 697, 222], [], [], [158, 437, 513, 503], [158, 437, 513, 503], [158, 437, 513, 503], [158, 437, 513, 503], [158, 437, 513, 503], [], [158, 437, 513, 503], [], [], [255, 158, 437, 513, 391, 503, 630, 105, 89], [255, 158, 437, 513, 391, 503, 630, 105, 89], [89], [89], [], [255, 158, 437, 513, 391, 503, 630, 105], [391], [391], [], [255, 158, 437, 513, 503, 630, 105], [630], [630], [], [255, 158, 437, 513, 503, 105], [255], [255], [], [158, 437, 513, 503, 105], [105], [105], [], [158, 437, 513, 503], [], [], [], [], [], [], [], [437], [437], [437], [], []]