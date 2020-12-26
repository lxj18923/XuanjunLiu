import dva from 'dva';
import createLoading from 'dva-loading';

const runtimeDva = window.g_plugins.mergeConfig('dva');
let app = dva({
  history: window.g_history,
  
  ...(runtimeDva.config || {}),
});

window.g_app = app;
app.use(createLoading());
(runtimeDva.plugins || []).forEach(plugin => {
  app.use(plugin);
});

app.model({ namespace: 'comment', ...(require('D:/Desktop/mark/src/models/comment/comment.js').default) });
app.model({ namespace: 'global', ...(require('D:/Desktop/mark/src/models/global.js').default) });
app.model({ namespace: 'list', ...(require('D:/Desktop/mark/src/models/list.js').default) });
app.model({ namespace: 'label', ...(require('D:/Desktop/mark/src/models/mark/label.js').default) });
app.model({ namespace: 'list', ...(require('D:/Desktop/mark/src/models/mark/list.js').default) });
app.model({ namespace: 'proofreading', ...(require('D:/Desktop/mark/src/models/mark/proofreading.js').default) });
app.model({ namespace: 'search', ...(require('D:/Desktop/mark/src/models/mark/search.js').default) });
app.model({ namespace: 'project', ...(require('D:/Desktop/mark/src/models/project.js').default) });
app.model({ namespace: 'setting', ...(require('D:/Desktop/mark/src/models/setting.js').default) });
app.model({ namespace: 'statistics', ...(require('D:/Desktop/mark/src/models/statistics.js').default) });
app.model({ namespace: 'triad', ...(require('D:/Desktop/mark/src/models/triad/triad.js').default) });
app.model({ namespace: 'login', ...(require('D:/Desktop/mark/src/models/user/login.js').default) });
app.model({ namespace: 'permission', ...(require('D:/Desktop/mark/src/models/user/permission.js').default) });
app.model({ namespace: 'role', ...(require('D:/Desktop/mark/src/models/user/role.js').default) });
app.model({ namespace: 'user', ...(require('D:/Desktop/mark/src/models/user/user.js').default) });
