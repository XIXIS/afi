import axios from 'axios';
import FuseUtils from "../@fuse/FuseUtils";

class AuthService extends FuseUtils.EventEmitter {

  constructor() {
    super();
    this.setInterceptors();
  }

  setInterceptors = () => {
    axios.interceptors.response.use(response => {
      return response;
    }, err => {
      return new Promise((resolve, reject) => {
        if (err.response.status === 401 && err.config && !err.config.__isRetryRequest) {
          // if you ever get an unauthorized response, logout the user
          this.emit('onAutoLogout', 'Invalid access_token');
          this.setSession(null);
        }
        throw err;
      });
    });
  };


  signInWithEmailAndPassword = (email, password) => {
    return new Promise((resolve, reject) => {
      axios.post(`${process.env.REACT_APP_BASE_URL}/auth/login`, {
        email,
        password
      }).then(response => {
        localStorage.setItem('BHUser', JSON.stringify(response.data.data));
        this.setSession(response.data.token);
        resolve(response.data.data);
      }).catch(err => {
        console.log(err);
        reject(err.response ? err.response.data.message : err.message);
      });
    });
  };


  setSession = accessToken => {
    if (accessToken) {
      localStorage.setItem('BHAccessToken', accessToken);
      axios.defaults.headers.common['Authorization'] = accessToken;
    } else {
      localStorage.removeItem('BHAccessToken');
      localStorage.removeItem('BHUser');
      delete axios.defaults.headers.common['Authorization'];
    }
  };

  logout = () => {
    this.setSession(null);
  };

}

let instance = new AuthService();

export default instance;