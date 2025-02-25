import axios from 'axios';

const http = axios.create({

    baseURL: "https://accounts.spotify.com/"
});

const apiHttp = axios.create({
    baseURL: "https://api.spotify.com/v1/"
})

export default{

    getToken() {

        const grantsBody = new URLSearchParams();
        grantsBody.append('grant_type', 'client_credentials');

        return http.post('api/token', grantsBody, 
        { 
            auth: {
                username : '223feb65c9eb4d549b0d4b44eb859bda',
                password: '0f1f4b5129e74e9f88cecd96a7c461fa'
            }
        });
    },

    getPlaylist(token) {
        return apiHttp.get('playlists/3gV9fNBHxYroyN2xGp91tN', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
    },

    getArtistGenres(token, artistId) {
        return apiHttp.get(`artists/${artistId}`,{
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
    }

}