export
const environment = {
  production: false,

  refreshToken: 'http://localhost:8080/login/refresh-token',
  logout: '',

  adminConnexionUrl: 'http://localhost:8080/login/admin',
  candidatConnexionUrl: 'http://localhost:8080/login/candidat',

  GetAllQuestionUrl: 'http://localhost:8080/question/all',
  addQuestion: 'http://localhost:8080/question/add',
  deleteQuestion: 'http://localhost:8080/question/delete/',
  modifyQuestion: 'http://localhost:8080/question/modify/',

  addReponse: 'http://localhost:8080/reponse/add',
  deleteReponse: 'http://localhost:8080/reponse/delete/',
  modifyReponse: 'http://localhost:8080/reponse/modify/',

  addQcm: 'http://localhost:8080/qcm/add',
  deleteQcm: 'http://localhost:8080/qcm/delete/',
  addQuestionToQcm:'http://localhost:8080/qcm/add-question',
  getAllQcms: 'http://localhost:8080/qcm/all',
  updateOrdreQcmQuestion: 'http://localhost:8080/qcm/update-order',

  addSandbox: 'http://localhost:8080/sandbox/add',

  addEntretien: 'http://localhost:8080/entretien/add',
  getEntretien: 'http://localhost:8080/entretien/',

  addCandidat: 'http://localhost:8080/personne/add-candidat',
  getAllCandidats: 'http://localhost:8080/personne/get-candidat',
}
