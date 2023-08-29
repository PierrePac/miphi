export
const environment = {
  production: false,

  refreshToken: 'http://localhost:8080/login/refresh-token',
  logout: '',

  adminConnexionUrl: 'http://localhost:8080/login/admin',
  candidatConnexionUrl: 'http://localhost:8080/login/candidat',

  GetAllQuestionUrl: 'http://localhost:8080/question/all',
  GetAllQuestionUrlWr: 'http://localhost:8080/question/all-wr',
  getQuestionFromQcm: 'http://localhost:8080/question/get-by-qcm/',
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
  getOneQcm:'http://localhost:8080/qcm/get-by-entretien/',
  getQcmQuestion: 'http://localhost:8080/question-qcm/get/',
  getQcmQuestionByEntretien: 'http://localhost:8080/question-qcm/get-by-entretien/',
  updateOrdreQcmQuestion: 'http://localhost:8080/question-qcm/update-order',

  addSandbox: 'http://localhost:8080/sandbox/add',

  addEntretien: 'http://localhost:8080/entretien/add',
  getEntretien: 'http://localhost:8080/entretien/',

  addAdmin: 'http://localhost:8080/personne/add-admin',
  addCandidat: 'http://localhost:8080/personne/add-candidat',
  getAllCandidats: 'http://localhost:8080/personne/get-candidat',

  postQcmReponses: 'http://localhost:8080/reponse-candidat/saveAll',
  getQcmReponsesOfCandidat: 'http://localhost:8080/reponse-candidat/get-by-candidat/',
}
