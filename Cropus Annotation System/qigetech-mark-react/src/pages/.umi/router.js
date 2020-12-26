import React from 'react';
import { Router as DefaultRouter, Route, Switch } from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/_renderRoutes';
import RendererWrapper0 from 'D:/Desktop/mark/src/pages/.umi/LocaleWrapper.jsx'
import _dvaDynamic from 'dva/dynamic'

let Router = require('dva/router').routerRedux.ConnectedRouter;

let routes = [
  {
    "path": "/user",
    "component": _dvaDynamic({
  
  component: () => import('../../layouts/UserLayout'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
    "routes": [
      {
        "path": "/user",
        "redirect": "/user/login",
        "exact": true
      },
      {
        "path": "/user/login",
        "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/User/models/register.js').then(m => { return { namespace: 'register',...m.default}})
],
  component: () => import('../User/Login'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
        "exact": true
      },
      {
        "path": "/user/register",
        "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/User/models/register.js').then(m => { return { namespace: 'register',...m.default}})
],
  component: () => import('../User/Register'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
        "exact": true
      },
      {
        "path": "/user/register-result",
        "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/User/models/register.js').then(m => { return { namespace: 'register',...m.default}})
],
  component: () => import('../User/RegisterResult'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
        "exact": true
      },
      {
        "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
      }
    ]
  },
  {
    "path": "/",
    "component": _dvaDynamic({
  
  component: () => import('../../layouts/BasicLayout'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
    "Routes": [require('../Authorized').default],
    "authority": [
      "admin",
      "user"
    ],
    "routes": [
      {
        "path": "/",
        "redirect": "/dashboard/analysis",
        "exact": true
      },
      {
        "path": "/dashboard",
        "name": "Dashboard",
        "icon": "dashboard",
        "routes": [
          {
            "path": "/dashboard/analysis",
            "name": "Analysis",
            "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Dashboard/models/activities.js').then(m => { return { namespace: 'activities',...m.default}}),
  import('D:/Desktop/mark/src/pages/Dashboard/models/chart.js').then(m => { return { namespace: 'chart',...m.default}}),
  import('D:/Desktop/mark/src/pages/Dashboard/models/monitor.js').then(m => { return { namespace: 'monitor',...m.default}})
],
  component: () => import('../Dashboard/Analysis'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/dashboard/statistics",
            "name": "Statistics",
            "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Dashboard/models/activities.js').then(m => { return { namespace: 'activities',...m.default}}),
  import('D:/Desktop/mark/src/pages/Dashboard/models/chart.js').then(m => { return { namespace: 'chart',...m.default}}),
  import('D:/Desktop/mark/src/pages/Dashboard/models/monitor.js').then(m => { return { namespace: 'monitor',...m.default}})
],
  component: () => import('../Dashboard/Statistics'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
          }
        ]
      },
      {
        "name": "exception",
        "icon": "warning",
        "path": "/exception",
        "hideInMenu": true,
        "routes": [
          {
            "path": "/exception/403",
            "name": "not-permission",
            "hideInMenu": true,
            "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Exception/models/error.js').then(m => { return { namespace: 'error',...m.default}})
],
  component: () => import('../Exception/403'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/exception/404",
            "name": "not-find",
            "hideInMenu": true,
            "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Exception/models/error.js').then(m => { return { namespace: 'error',...m.default}})
],
  component: () => import('../Exception/404'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/exception/500",
            "name": "server-error",
            "hideInMenu": true,
            "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Exception/models/error.js').then(m => { return { namespace: 'error',...m.default}})
],
  component: () => import('../Exception/500'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/exception/trigger",
            "name": "trigger",
            "hideInMenu": true,
            "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Exception/models/error.js').then(m => { return { namespace: 'error',...m.default}})
],
  component: () => import('../Exception/TriggerException'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
          }
        ]
      },
      {
        "name": "account",
        "icon": "user",
        "path": "/account",
        "hideInMenu": true,
        "routes": [
          {
            "path": "/account/center",
            "name": "center",
            "component": _dvaDynamic({
  
  component: () => import('../Account/Center/Center'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "routes": [
              {
                "path": "/account/center",
                "redirect": "/account/center/articles",
                "exact": true
              },
              {
                "path": "/account/center/articles",
                "component": _dvaDynamic({
  
  component: () => import('../Account/Center/Articles'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
                "exact": true
              },
              {
                "path": "/account/center/applications",
                "component": _dvaDynamic({
  
  component: () => import('../Account/Center/Applications'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
                "exact": true
              },
              {
                "path": "/account/center/projects",
                "component": _dvaDynamic({
  
  component: () => import('../Account/Center/Projects'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
                "exact": true
              },
              {
                "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
              }
            ]
          },
          {
            "path": "/account/settings",
            "name": "settings",
            "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Account/Settings/models/geographic.js').then(m => { return { namespace: 'geographic',...m.default}})
],
  component: () => import('../Account/Settings/Info'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "routes": [
              {
                "path": "/account/settings",
                "redirect": "/account/settings/base",
                "exact": true
              },
              {
                "path": "/account/settings/base",
                "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Account/Settings/models/geographic.js').then(m => { return { namespace: 'geographic',...m.default}})
],
  component: () => import('../Account/Settings/BaseView'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
                "exact": true
              },
              {
                "path": "/account/settings/security",
                "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Account/Settings/models/geographic.js').then(m => { return { namespace: 'geographic',...m.default}})
],
  component: () => import('../Account/Settings/SecurityView'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
                "exact": true
              },
              {
                "path": "/account/settings/binding",
                "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Account/Settings/models/geographic.js').then(m => { return { namespace: 'geographic',...m.default}})
],
  component: () => import('../Account/Settings/BindingView'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
                "exact": true
              },
              {
                "path": "/account/settings/notification",
                "component": _dvaDynamic({
  app: window.g_app,
models: () => [
  import('D:/Desktop/mark/src/pages/Account/Settings/models/geographic.js').then(m => { return { namespace: 'geographic',...m.default}})
],
  component: () => import('../Account/Settings/NotificationView'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
                "exact": true
              },
              {
                "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
              }
            ]
          },
          {
            "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
          }
        ]
      },
      {
        "name": "User-Management",
        "icon": "user",
        "path": "/user-management",
        "routes": [
          {
            "path": "/user-management/role",
            "name": "Role",
            "component": _dvaDynamic({
  
  component: () => import('../Management/user/role'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/user-management/permission",
            "name": "Permission",
            "component": _dvaDynamic({
  
  component: () => import('../Management/user/permission'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/user-management/info",
            "name": "Info",
            "component": _dvaDynamic({
  
  component: () => import('../Management/user/info'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
          }
        ]
      },
      {
        "name": "Corpus Annotation",
        "icon": "user",
        "path": "/mark",
        "routes": [
          {
            "path": "/mark/label",
            "name": "Annotation",
            "component": _dvaDynamic({
  
  component: () => import('../Management/mark/label'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/mark/selectLabel",
            "name": "Select Annotation",
            "component": _dvaDynamic({
  
  component: () => import('../Management/mark/selectLabel'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "hideInMenu": true,
            "exact": true
          },
          {
            "path": "/mark/list",
            "name": "Annotation Result List",
            "component": _dvaDynamic({
  
  component: () => import('../Management/mark/list'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/mark/search",
            "name": "Search",
            "component": _dvaDynamic({
  
  component: () => import('../Management/mark/search'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/mark/comment",
            "name": "Message",
            "component": _dvaDynamic({
  
  component: () => import('../Management/comment/List'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "path": "/mark/triad/list",
            "name": "triad",
            "hideInMenu": true,
            "component": _dvaDynamic({
  
  component: () => import('../Management/triad/list'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
            "exact": true
          },
          {
            "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
          }
        ]
      },
      {
        "component": _dvaDynamic({
  
  component: () => import('../404'),
  LoadingComponent: require('D:/Desktop/mark/src/components/PageLoading/index').default,
}),
        "exact": true
      },
      {
        "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
      }
    ]
  },
  {
    "component": () => React.createElement(require('D:/Desktop/mark/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
  }
];
window.g_routes = routes;
window.g_plugins.applyForEach('patchRoutes', { initialValue: routes });

// route change handler
function routeChangeHandler(location, action) {
  window.g_plugins.applyForEach('onRouteChange', {
    initialValue: {
      routes,
      location,
      action,
    },
  });
}
window.g_history.listen(routeChangeHandler);
routeChangeHandler(window.g_history.location);

export default function RouterWrapper() {
  return (
<RendererWrapper0>
          <Router history={window.g_history}>
      { renderRoutes(routes, {}) }
    </Router>
        </RendererWrapper0>
  );
}
