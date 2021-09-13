'use strict';

angular.module('app',['ngRoute','ngResource','routeStyles','ngCookies','ngMessages'])

.config(function($routeProvider,$httpProvider){
    $routeProvider

    .when('/books/:nr',{
        templateUrl:'partials/main.html',
        controller:'BrowserController',
        controllerAs:'brCtrl',
        css: 'css/books.css'
    })

    .when('/info',{
        templateUrl:'partials/info.html',
        controller:'infoController',
        controllerAs:'infoCtrl',
        css: 'css/books.css'
    })

    .when('/details/:id',{
        templateUrl:'partials/details.html',
        controller: 'detailsBooksController', 
        controllerAs:'detailsCtrl',
        css: 'css/books.css'
    })

    .when('/addBook',{
        templateUrl:'partials/addbook.html',
        controller:'addNewBookController',
        controllerAs:'addNewBookCtrl',
        css: 'css/books.css'
    })

    .when('/registration',{
        templateUrl:'partials/registration.html',
        controller:'RegistrationController',
        controllerAs:'regCtrl',
        css: 'css/books.css'
    })

    .when('/registrationConfirmed',{
        templateUrl:'partials/registrationConfirmed.html',
        controller:'ConfirmedController',
        controllerAs:'confirmedCtrl',
        css: 'css/books.css'
    })

    .otherwise({
        redirectTo:'/info'
    })

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

})

.run(function($http,$cookies,$rootScope){  
    $http.defaults.headers.post.Authorization = $cookies.get('AuthCookie');

    if($cookies.get('AuthCookie')!==undefined){
        $rootScope.authenticated = true;
    } else {
        $rootScope.authenticated = false;
    }
})

.constant('BooksController','/api/books/:id')
.factory('Book',function($resource,BooksController){
    return $resource(BooksController);
})

.service('Books',function(Book){
    this.getAll = function(){
        return Book.query();
    }

    this.get = function(index){
        return Book.get({id:index});
    }

    this.addBook = function(book){
        console.log(book);
        book.$save();
    }
})

.controller ('infoController',function(Books){
    var vm = this;
    var bookid = '2';
    console.log(bookid);
    vm.books=Books.get('2');
})

.controller ('addNewBookController',function(Books,Book){

    var vm = this;
    vm.book = new Book();
    vm.addNewBook = function(){
        Books.addBook(vm.book);
        vm.book = new Book();
    }
})

.controller ('detailsBooksController',function($routeParams, Books){
    var vm = this;
    var bookId = $routeParams.id;
    vm.books = Books.get(bookId);

    console.log(vm.books);
})

.constant('BooksCommentConstant', '/api/books/addComment/:idComment')
.factory('BooksCommentFactory',function($resource,BooksCommentConstant){
    return $resource(BooksCommentConstant);
})

.service('BoooksComment',function($route){
    this.addComment = function (comment){
        comment.$save({idComment:comment.bookId},

            function success(data){
                $route.reload();
            },
            function error(response){
                console.log(response.status);
            });
    }
})

.controller('commentBooksController',function(BoooksComment,BooksCommentFactory,$routeParams){
    
    var vm = this;
    vm.comment = new BooksCommentFactory();
    vm.comment.bookId = $routeParams.id;
    vm.saveComment = function(){
        BoooksComment.addComment(vm.comment);
        vm.comment = new BooksCommentFactory();
    }
})

.factory('AllBrowserBookOnPage',function($resource){
    return $resource(
        '/api/books/page/:nr/:brow',
        {method: 'getTast', q: '*'},
        {'query' : {method: 'GET', isArray: false}}
    )
})

.service('BrowserBooksPage', function(AllBrowserBookOnPage){
    
    this.getAllBrowserOnPage = function(number,value){

        var browserBookData = AllBrowserBookOnPage.query({nr:number,brow:value},
            function success(data){
            },
            function error(response){
                console.log(response.status);
            }
            );
        return browserBookData
            }
})

.controller ('BrowserController',function($routeParams,BrowserBooksPage,PageBar,$cookies,$location){

    var vm = this;
    var value = 'all';
    if($cookies.get('query')!==undefined){
        value = $cookies.get('query');
    } 
    var pageNr = $routeParams.nr;
    if(pageNr===undefined){
        pageNr = 0;
    } 

    vm.search = function(){  
        $cookies.put('query',vm.data);
        $location.path('/!books/0');
        vm.data='';
    }

    vm.all = function(){
        $cookies.remove('query');
        $location.path('#!books/0');
    }

    vm.books=BrowserBooksPage.getAllBrowserOnPage(pageNr,value);

    if((vm.books!==undefined) || (vm.books.totalPages>1)){

    vm.books.$promise.then(function(){
        vm.page = PageBar.getPageBar(vm.books.totalPages,pageNr);
    })   }    
})

.factory('PageBarData',function(){
   return function PageBarData(pageAdress,pageMarker,pageActive){
            this.pageAdress = pageAdress;
            this.pageMarker = pageMarker;
            this.pageActive = pageActive;
    }
})

.service('PageBar',function(PageBarData,$rootScope){    

    this.getPageBar = function(totalPages,actualPage){

        this.totalPages = totalPages;
        this.actualPage = actualPage;
        var httpAdress = '#!/books/';
        var active;

        if (totalPages > 1){
            $rootScope.pageBarShow = true;
        var pageNrs = [];  
            for(var i=0; i<totalPages;i++){
                if(i==actualPage){active = 'pageNr active'} else {active = 'pageNr'};
                pageNrs.push(new PageBarData(httpAdress+i,i+1,active));
            }
        return pageNrs;}
        else {
            $rootScope.pageBarShow = false;
        }
    }
})


//photo upload

.controller('UploadFileController',function($scope,$http){
  
        var Book = function (title,author,category){
            this.title = title;
            this.author = author;
            this.category = category;
        }

        var vm = this;
        vm.book = new Book();

      $scope.uploadFile = function(){

            $http({
                method: 'POST',
                url: "/api/books",
                headers : {'Content-Type':undefined},
                transformRequest: function (data){
                    var formData = new FormData();
                    formData.append("book",angular.toJson(data.book));
                    formData.append("file",data.file);
                    return formData;
                },
                data : {book : vm.book, file: $scope.myForm.file}
            });   
      };
})

.directive('fileModel',['$parse',function($parse){
    return {
        restric : 'A',
        link : function(scope,element,attrs){
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope,element[0].files[0]);
                });
            });
        }
    };

}])

// logowanie

.constant('LOGIN_ENDPOINT','/login')
.service('AuthenticationService', function($http, LOGIN_ENDPOINT, $cookies,$rootScope) {
    this.authenticate = function(cred, successCallback) {
        var authHeader = {Authorization: 'Basic ' + btoa(cred.username+':'+cred.password)};
        var config = {headers: authHeader};
        $http
        .post(LOGIN_ENDPOINT, {}, config)
        .then(function success(value) {

            $cookies.put('AuthCookie',authHeader.Authorization);
            $http.defaults.headers.post.Authorization = authHeader.Authorization;

            console.log(value);
 			successCallback();

        }, function error(reason) {
            console.log('Login error');
            console.log(reason);
        });
    }

    this.logout = function(successCallback){
        console.log("wyczyszczenie storage");
        $cookies.remove('AuthCookie');
        $rootScope.authenticated = false;
        delete $http.defaults.headers.post.Authorization;
        successCallback();
    }
})

.controller('AuthenticationController', function($rootScope, $location, AuthenticationService) {
    var vm = this;
    vm.cred = {};

    var loginSuccess = function() {
        console.log('autoryzacja ok');
        $rootScope.authenticated = true;
        // $location.path('/');
    }

    var  logoutSuccess = function() {
        $location.path('/');
    }

    vm.login = function() {
        AuthenticationService.authenticate(vm.cred, loginSuccess);
    }

    vm.logout = function() {
        AuthenticationService.logout(logoutSuccess);        
    }
})

//rejestracja

.service('RegistrationService',function($http){
    
    this.addNewUser = function addNewUser(user){

        return $http({
            method: 'POST',
            url: "/registration",
            params : user,
            headers : {'Content-Type':'application/json'}
        });
    }
})

.controller('RegistrationController', function(RegistrationService,$scope){

    var vm = this;
    vm.User = {};
    var errors;
    $scope.submitted = false;
   
    vm.createAcc = function(){

        console.log('proba wyslania');
        $scope.submitted = true;

        if($scope.userForm.$invalid && errors!==undefined){

        for(var i=0; i<errors.length; i++){
            $scope.userForm.username.$setValidity(errors[i].code,true);
            $scope.userForm.email.$setValidity(errors[i].code,true);
            $scope.userForm.password.$setValidity(errors[i].code,true);
            $scope.userForm.matchingPassword.$setValidity(errors[i].code,true);
        }}

        if($scope.userForm.$valid){

        RegistrationService.addNewUser(vm.User)
            .then (function success(response){

                console.log(response);
                $scope.submitted = false;
            },
            function error(response){

                console.log(response);

                vm.errors = response.data;
                errors = response.data;

                console.log(errors[0].defaultMessage);
                console.log(errors[0].code);

                    for(var i=0; i < errors.length; i++)
                    {
                        var fieldId = errors[0].field;
                        console.log(fieldId)

                        switch(fieldId){
                            case 'username':
                                console.log('username');
                                $scope.userForm.username.$setValidity(errors[i].code,false);
                            break;
                            case 'email':
                                console.log('email');
                                $scope.userForm.email.$setValidity(errors[i].code,false);
                            break;
                            case 'password':
                                console.log('pass');
                                $scope.userForm.password.$setValidity(errors[i].code,false);
                            break;
                            case 'matchingPassword':
                                console.log('mpass');
                                $scope.userForm.matchingPassword.$setValidity(errors[i].code,false);
                            break;
                            default:
                                console.log('poza petla');
                        }

                    } 
            }   
            )               
    }
};
})

.controller('ConfirmedController', function($location,$timeout){

    $timeout(function(){
    $location.path('/')
    },7000);

});