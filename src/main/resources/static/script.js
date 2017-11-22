var air = angular.module('air', ['highcharts-ng', 'ngResource']);

air.factory('Chart', function ($resource) {
    return $resource('/:action/:id', {}, {
        chart: {method: 'GET', isArray: true, params: {action: 'chart'}}
    });
});

air.controller('ChartsCtrl', function ChartsCtrl($scope, Chart) {
    var psps = $scope.psps = ['psp1', 'psp2'];
    var bins = $scope.bins = ['bin1', 'bin2', 'bin3'];
    // var chartsData = $scope.chartsData = {};
    var chartsCfgs = $scope.chartsCfgs = {};

    $scope.total = psps.length * bins.length;
    $scope.loaded = 0;

    for (var i = 0; i < psps.length; i++) {
        for (var j = 0; j < bins.length; j++) {
            (function (bin, psp) {
                Chart.chart({bin: bin, psp: psp, n: 100}, function (res) {
                    $scope.loaded++;

                    var vals = [];
                    for (var k = 0; k < res.length; k++) {
                        vals.push(res[k].y);
                    }

                    // (chartsData[bin] || (chartsData[bin] = {}))[psp] = res;
                    (chartsCfgs[bin] || (chartsCfgs[bin] = {}))[psp] = {
                        title: {
                            text: ''
                        },
                        chart: {
                            type: 'area'
                            // type: 'line'
                        },
                        xAxis: {
                            categories: []
                        },
                        yAxis: {
                            min: 0,
                            title: {text: ''},
                            plotLines: [
                                {
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }
                            ]
                        },
                        series: [
                            {
                                name: 'P',
                                color: psp === 'psp1' ? '#77ff77' : '#7777ff',
                                data: vals
                            }
                        ]
                    };
                })
            })(bins[j], psps[i]);
        }
    }
});