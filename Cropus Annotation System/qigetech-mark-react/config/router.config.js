export default [
  // user
  {
    path: "/user",
    component: "../layouts/UserLayout",
    routes: [
      { path: "/user", redirect: "/user/login" },
      { path: "/user/login", component: "./User/Login" },
      { path: "/user/register", component: "./User/Register" },
      { path: "/user/register-result", component: "./User/RegisterResult" }
    ]
  },
  // app
  {
    path: "/",
    component: "../layouts/BasicLayout",
    Routes: ["src/pages/Authorized"],
    authority: ["admin", "user"],
    routes: [
      // dashboard
      { path: "/", redirect: "/dashboard/analysis" },
      {
        path: "/dashboard",
        name: "Dashboard",
        icon: "dashboard",
        routes: [
          {
            path: "/dashboard/analysis",
            name: "Analysis",
            component: "./Dashboard/Analysis"
          },
          {
            path: "/dashboard/statistics",
            name: "Statistics",
            component: "./Dashboard/Statistics"
          }
        ]
      },
      {
        name: "exception",
        icon: "warning",
        path: "/exception",
        hideInMenu: true,
        routes: [
          // exception
          {
            path: "/exception/403",
            name: "not-permission",
            hideInMenu: true,
            component: "./Exception/403"
          },
          {
            path: "/exception/404",
            name: "not-find",
            hideInMenu: true,
            component: "./Exception/404"
          },
          {
            path: "/exception/500",
            name: "server-error",
            hideInMenu: true,
            component: "./Exception/500"
          },
          {
            path: "/exception/trigger",
            name: "trigger",
            hideInMenu: true,
            component: "./Exception/TriggerException"
          }
        ]
      },
      {
        name: "account",
        icon: "user",
        path: "/account",
        hideInMenu: true,
        routes: [
          {
            path: "/account/center",
            name: "center",
            component: "./Account/Center/Center",
            routes: [
              {
                path: "/account/center",
                redirect: "/account/center/articles"
              },
              {
                path: "/account/center/articles",
                component: "./Account/Center/Articles"
              },
              {
                path: "/account/center/applications",
                component: "./Account/Center/Applications"
              },
              {
                path: "/account/center/projects",
                component: "./Account/Center/Projects"
              }
            ]
          },
          {
            path: "/account/settings",
            name: "settings",
            component: "./Account/Settings/Info",
            routes: [
              {
                path: "/account/settings",
                redirect: "/account/settings/base"
              },
              {
                path: "/account/settings/base",
                component: "./Account/Settings/BaseView"
              },
              {
                path: "/account/settings/security",
                component: "./Account/Settings/SecurityView"
              },
              {
                path: "/account/settings/binding",
                component: "./Account/Settings/BindingView"
              },
              {
                path: "/account/settings/notification",
                component: "./Account/Settings/NotificationView"
              }
            ]
          }
        ]
      },
      {
        name: "User-Management",
        icon: "user",
        path: "/user-management",
        routes: [
          // exception
          {
            path: "/user-management/role",
            name: "Role",
            component: "./Management/user/role"
          },
          {
            path: "/user-management/permission",
            name: "Permission",
            component: "./Management/user/permission"
          },
          {
            path: "/user-management/info",
            name: "Info",
            component: "./Management/user/info"
          }
        ]
      },
      {
        name: "Corpus Annotation",
        icon: "user",
        path: "/mark",
        routes: [
          // exception
          {
            path: "/mark/label",
            name: "Annotation",
            component: "./Management/mark/label"
          },
          {
            path: "/mark/selectLabel",
            name: "Select Annotation",
            component: "./Management/mark/selectLabel",
            hideInMenu: true
          },
          // {
          //   path: '/mark/proofreading',
          //   name: 'proofreading',
          //   component: './Management/mark/proofreading',
          // },
          {
            path: "/mark/list",
            name: "Annotation Result List",
            component: "./Management/mark/list"
          },
          {
            path: "/mark/search",
            name: "Search",
            component: "./Management/mark/search"
          },
          // {
          //   path: "/mark/upload",
          //   name: "Upload",
          //   component: "./Management/mark/upload"
          // },
          {
            path: "/mark/comment",
            name: "Message",
            component: "./Management/comment/List"
          },
          {
            path: "/mark/triad/list",
            name: "triad",
            hideInMenu: true,
            component: "./Management/triad/list"
          }
          // {
          //     path: '/mark/triad',
          //     name: 'triad',
          //     hideInMenu: false,
          //     component: './Management/triad/mark',
          // }
        ]
      },
      {
        component: "404"
      }
    ]
  }
];
