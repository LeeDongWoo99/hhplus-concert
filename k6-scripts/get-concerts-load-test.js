import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 50,
    duration: '30s',
};

// 부하 테스트 시작 전에 콘서트 더미 데이터 10개를 생성
export function setup() {
    const concertIds = [];
    for (let i = 1; i <= 10; i++) {
        const payload = JSON.stringify({
            name: `테스트 콘서트 ${i}`,
            date: '2025-07-01',
            location: '서울',
            // 필요한 추가 필드가 있다면 여기에 추가
        });

        const headers = { 'Content-Type': 'application/json' };
        const res = http.post('http://localhost:8080/?page=${page}', payload, { headers });

        check(res, {
            '콘서트 생성 status 201': (r) => r.status === 201,
        });

        // 생성된 콘서트 ID를 저장 (필요하다면)
        if (res.status === 201 && res.json('id')) {
            concertIds.push(res.json('id'));
        }
    }
    return { concertIds };
}

// 실제 부하 테스트: 콘서트 목록 조회
export default function (data) {
    const page = Math.floor(Math.random() * 5) + 1; // 1~5페이지 랜덤 조회
    const url = `http://localhost:8080/concerts/?page=${page}`;
    const res = http.get(url);

    check(res, {
        'status is 200': (r) => r.status === 200,
        'concerts 데이터 있음': (r) => r.json('data') !== undefined,
    });

    sleep(1);
}
