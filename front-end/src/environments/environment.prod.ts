export
const environment = {
  production: true,

  refreshToken: 'http://p2lxtdkear01.mipih.net:8282/login/refresh-token',
  logout: '',

  adminConnexionUrl: 'http://p2lxtdkear01.mipih.net:8282/login/admin',
  candidatConnexionUrl: 'http://p2lxtdkear01.mipih.net:8282/login/candidat',

  GetAllQuestionUrl: 'http://p2lxtdkear01.mipih.net:8282/question/all',
  GetAllQuestionUrlWr: 'http://p2lxtdkear01.mipih.net:8282/question/all-wr',
  getQuestionFromQcm: 'http://p2lxtdkear01.mipih.net:8282/question/get-by-qcm/',
  addQuestion: 'http://p2lxtdkear01.mipih.net:8282/question/add',
  deleteQuestion: 'http://p2lxtdkear01.mipih.net:8282/question/delete/',
  modifyQuestion: 'http://p2lxtdkear01.mipih.net:8282/question/modify/',

  addReponse: 'http://p2lxtdkear01.mipih.net:8282/reponse/add',
  deleteReponse: 'http://p2lxtdkear01.mipih.net:8282/reponse/delete/',
  modifyReponse: 'http://p2lxtdkear01.mipih.net:8282/reponse/modify/',

  addQcm: 'http://p2lxtdkear01.mipih.net:8282/qcm/add',
  deleteQcm: 'http://p2lxtdkear01.mipih.net:8282/qcm/delete/',
  addQuestionToQcm:'http://p2lxtdkear01.mipih.net:8282/qcm/add-question',
  getAllQcms: 'http://p2lxtdkear01.mipih.net:8282/qcm/all',
  getOneQcm:'http://p2lxtdkear01.mipih.net:8282/qcm/get-by-entretien/',
  getQcmQuestion: 'http://p2lxtdkear01.mipih.net:8282/question-qcm/get/',
  getQcmQuestionByEntretien: 'http://p2lxtdkear01.mipih.net:8282/question-qcm/get-by-entretien/',
  updateOrdreQcmQuestion: 'http://p2lxtdkear01.mipih.net:8282/question-qcm/update-order',

  addSandbox: 'http://p2lxtdkear01.mipih.net:8282/sandbox/add',
  getAllSandbox: 'http://p2lxtdkear01.mipih.net:8282/sandbox/get-all',

  addEntretien: 'http://p2lxtdkear01.mipih.net:8282/entretien/add',
  getEntretien: 'http://p2lxtdkear01.mipih.net:8282/entretien/',
  getAllEntretien: 'http://p2lxtdkear01.mipih.net:8282/entretien/get-all',

  addAdmin: 'http://p2lxtdkear01.mipih.net:8282/personne/add-admin',
  addCandidat: 'http://p2lxtdkear01.mipih.net:8282/personne/add-candidat',
  getAllCandidats: 'http://p2lxtdkear01.mipih.net:8282/personne/get-candidat',

  postQcmReponses: 'http://p2lxtdkear01.mipih.net:8282/reponse-candidat/saveAll',
  getQcmReponsesOfCandidat: 'http://p2lxtdkear01.mipih.net:8282/reponse-candidat/get-by-candidat/',

}
