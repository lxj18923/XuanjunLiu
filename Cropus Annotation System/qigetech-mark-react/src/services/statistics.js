import request from '@/utils/request';

let url = "http://121.40.51.107:8087";
// let url = "http://106.14.7.183:8081";

export async function getWeekStatistics( ) {
    return request(url + `/count/all`);
}

export async function getUserList(payload) {
    console.log(payload)
    let url1 =url + "/count/stu";
    if (payload.current != null && payload.size != null) {
        url1 += "?current=" + payload.current + "&size=" + payload.size;
    }
    return request(url1);
}
