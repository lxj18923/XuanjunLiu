import dva from 'dva';
import { Component } from 'react';
import createLoading from 'dva-loading';
import history from '@tmp/history';

let app = null;

export function _onCreate() {
  const plugins = require('umi/_runtimePlugin');
  const runtimeDva = plugins.mergeConfig('dva');
  app = dva({
    history,
    
    ...(runtimeDva.config || {}),
    ...(window.g_useSSR ? { initialState: window.g_initialData } : {}),
  });
  
  app.use(createLoading());
  (runtimeDva.plugins || []).forEach(plugin => {
    app.use(plugin);
  });
  
  app.model({ namespace: 'comment', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/comment/comment.js').default) });
app.model({ namespace: 'global', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/global.js').default) });
app.model({ namespace: 'list', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/list.js').default) });
app.model({ namespace: 'label', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/mark/label.js').default) });
app.model({ namespace: 'list', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/mark/list.js').default) });
app.model({ namespace: 'proofreading', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/mark/proofreading.js').default) });
app.model({ namespace: 'search', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/mark/search.js').default) });
app.model({ namespace: 'project', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/project.js').default) });
app.model({ namespace: 'setting', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/setting.js').default) });
app.model({ namespace: 'statistics', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/statistics.js').default) });
app.model({ namespace: 'triad', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/triad/triad.js').default) });
app.model({ namespace: 'login', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/user/login.js').default) });
app.model({ namespace: 'permission', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/user/permission.js').default) });
app.model({ namespace: 'role', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/user/role.js').default) });
app.model({ namespace: 'user', ...(require('D:/project/mark-react-master/mark-react-gilt/src/models/user/user.js').default) });
  return app;
}

export function getApp() {
  return app;
}

export class _DvaContainer extends Component {
  render() {
    const app = getApp();
    app.router(() => this.props.children);
    return app.start()();
  }
}
