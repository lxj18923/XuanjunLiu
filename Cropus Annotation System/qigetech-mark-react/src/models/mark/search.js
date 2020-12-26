import {
    searchBar,
    uploadFile
} from '@/services/mark/search';


export default {
    namespace: 'search',
    state: {
        searchBarResult: '',
        uploadData: []
    },

    effects: {
        * upload({ payload,callback },{ call,put }){
            const response = yield call(uploadFile,payload);
            yield put({
                type: 'setUpload',
                payload: response
            });
            if(callback) callback(response)
        },
        * searchBar({ payload,callback },{ call,put }){
            const response = yield call(searchBar,payload);
            yield put({
                type: 'setSearchBar',
                payload: response,
            });
            if(callback) callback(response)
        },
    },

    reducers: {
        setUpload(state,action){
            return {
                ...state,
                uploadData: action.payload
            };
        },

        setSearchBar(state,action){
            return {
                ...state,
                searchBarResult: action.payload,
            };
        },
    },
};
