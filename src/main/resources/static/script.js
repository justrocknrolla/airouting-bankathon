var air = angular.module('air', ['highcharts-ng', 'ngResource']);

air.factory('Air', function ($resource) {
    return $resource('/:action/:id', {}, {
        chart: {method: 'GET', params: {action: 'chart'}},
        suggestPSP: {method: 'GET', params: {action: 'suggestPSP'}},
        train: {method: 'POST', params: {action: 'train'}},
        reset: {method: 'POST', params: {action: 'reset'}}
    });
});

air.controller('ChartsCtrl', function ChartsCtrl($scope, $interval, Air) {
    var psps = $scope.psps = ['adyen', 'wirecard'];
    var bins = $scope.bins = ['bin1', 'bin2', 'bin3'];
    var chartsCfgs = $scope.chartsCfgs = {};

    $scope.suggestPSP = function (bin) {
        Air.suggestPSP({bin: bin}, function (res) {
            $scope.suggested = {};
            $scope.suggested[bin] = res.suggested;
        })
    };

    $scope.train = function () {
        Air.train();
    };

    $scope.reset = function () {
        Air.reset();
    };

    $scope.total = psps.length * bins.length;

    function reload() {
        $scope.loaded = 0;

        for (var j = 0; j < bins.length; j++) {
            for (var i = 0; i < psps.length; i++) {
                (function (bin, psp) {
                    Air.chart({bin: bin, psp: psp, n: 60}, function (res) {
                        $scope.loaded++;

                        var points = res.points;
                        var stats = res.stats;

                        var average = (stats.average * 100).toFixed(0);
                        var deviation = (stats.deviation * 100).toFixed(0);

                        var vals = [];
                        for (var k = 0; k < points.length; k++) {
                            vals.push(points[k].y);
                        }

                        (chartsCfgs[bin] || (chartsCfgs[bin] = {}))[psp] = {
                            title: {
                                text: average + ' ± ' + deviation + ' %'
                            },
                            chart: {
                                type: 'area',
                                width: '350',
                                height: '245',
                                backgroundColor: '#f2f2f2',
                                animation: false
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
                                    name: 'Acceptance Rate',
                                    color: psp === 'adyen' ? '#77ff77' : '#7777ff',
                                    data: vals
                                }
                            ]
                        };
                    })
                })(bins[j], psps[i]);
            }
        }
    }

    reload();
    setInterval(reload, 1000);
});