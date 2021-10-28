angular.module('app').controller('mainCtrl',
    function ($scope, $http, $localStorage) {

    $scope.request = function() {
        $http({
              url: './request',
              method: 'POST',
              headers: {'Content-type': 'application/json'},
              data: $scope.Request,
              responseType: 'text/plain'
            })
            .then(function successCallback(response) {
                    console.log(response);
                    $scope.Response = response.data;
                    $scope.prettify(response.data);
            }, function errorCallback(response) {
                window.alert(response.data.message);
            });
    };

        $scope.prettify = function(data) {
            $scope.Pretty = JSON.stringify(data, null, 3);
        }
});
