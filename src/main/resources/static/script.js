var air = angular.module('air', ['highcharts-ng', 'ngResource']);

air.factory('Chart', function ($resource) {
    return $resource('/:action/:id', {}, {
        chart: {method: 'GET', isArray: true, params: {action: 'chart'}},
        stats: {method: 'GET', params: {action: 'stats'}}
    });
});

air.controller('ChartsCtrl', function ChartsCtrl($scope, $interval, Chart) {
    var psps = $scope.psps = ['adyen', 'wirecard'];
    var bins = $scope.bins = ['bin1', 'bin2', 'bin3'];
    // var chartsData = $scope.chartsData = {};
    var chartsCfgs = $scope.chartsCfgs = {};

    $scope.total = psps.length * bins.length;
    $scope.loaded = 0;

    for (var i = 0; i < psps.length; i++) {
        for (var j = 0; j < bins.length; j++) {
            (function (bin, psp) {
                var average = 0.0;
                var deviation = 0.0;
                Chart.stats({bin: bin, psp: psp}, function(result) {
                    average = (result.average * 100).toFixed(0);
                    deviation = (result.deviation * 100).toFixed(0);
                });


                Chart.chart({bin: bin, psp: psp, n: 100}, function (res) {
                    $scope.loaded++;

                    var vals = [];
                    for (var k = 0; k < res.length; k++) {
                        vals.push(res[k].y);
                    }

                    // (chartsData[bin] || (chartsData[bin] = {}))[psp] = res;
                    (chartsCfgs[bin] || (chartsCfgs[bin] = {}))[psp] = {
                        title: {
                            text: average + '% +/- ' + deviation + '%'
                        },
                        chart: {
                            type: 'area',
                            width: '300',
                            height: '200',
                            backgroundColor: '#f2f2f2'
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
                                color: psp === 'adyen' ? '#77ff77' : '#7777ff',
                                data: vals
                            }
                        ]
                    };
                })
            })(bins[j], psps[i]);
        }
    }
});