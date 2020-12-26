import request from '@/utils/request';

// let originUrl = "http://106.14.7.183:8081/origin";
// let labelResultUrl = "http://106.14.7.183:8081/label/result";
let originUrl = "http://121.40.51.107:8087/origin";
let labelResultUrl = "http://121.40.51.107:8087/label/result";

// let commentUrl = "http://106.14.7.183:8081/comment";
let commentUrl = "http://121.40.51.107:8087/comment";


export async function getList(payload) {
    let url = commentUrl + "/list";
    if (payload.pageNum != null && payload.pageSize != null) {
        url += "?current=" + payload.pageNum + "&size=" + payload.pageSize;
    }
    return request(url);
}

export async function submitComment(payload) {
    let data = {
        body: payload,
        method: "POST"
    };
    return request(commentUrl, data);
}
