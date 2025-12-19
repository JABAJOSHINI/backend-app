package com.marine.customerservice.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

//    private final PasswordEncryptionUtil encryptionUtil;
//    private final BCryptPasswordEncoder passwordEncoder;
//    private final LoginRepository loginRepository;
//    private final AuthenticationManager manager;
//    private final UserDetailsService userDetailsService;
//    private final JwtHelper helper;
//
//    private final Logger logger = (Logger) LoggerFactory.getLogger(LoginService.class);
//
//    @Autowired
//    public LoginService(PasswordEncryptionUtil encryptionUtil,
//                        LoginRepository loginRepository,
//                        AuthenticationManager manager,
//                        UserDetailsService userDetailsService,
//                        JwtHelper helper) {
//        this.encryptionUtil = encryptionUtil;
//        this.passwordEncoder = new BCryptPasswordEncoder(); // Initialize encoder
//        this.loginRepository = loginRepository;
//        this.manager = manager;
//        this.userDetailsService = userDetailsService;
//        this.helper = helper;
//    }
//
//    private void doAuthenticate(String email, String password) {
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
//        try {
//            manager.authenticate(authentication);
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException("Invalid Username or Password!");
//        }
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public String exceptionHandler() {
//        return "Invalid Credentials!";
//    }
//
//    public AuthModel login(LoginModel loginModel) throws Exception {
//        AuthModel authModel = new AuthModel();
//
//        // Authenticate user
//        this.doAuthenticate(loginModel.getEmail(), loginModel.getPassword());
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(loginModel.getUserName());
//        String token = this.helper.generateToken(userDetails);
//        System.out.println("Generated Token: " + token);
//
//        if (loginModel.getEmail() != null && loginModel.getPassword() != null) {
//            String decryptedPassword = encryptionUtil.decrypt(loginModel.getPassword());
//            System.out.println("Decrypted Password: " + decryptedPassword);
//
//            LoginEntity existingUser = loginRepository.findByEmail(loginModel.getEmail());
//            if (existingUser != null) {
//                // Use passwordEncoder.matches() instead of direct string comparison
//                if (passwordEncoder.matches(decryptedPassword, existingUser.getPassword())) {
//                    authModel.setToken(token);
//                    authModel.setUser(existingUser);
//                }
//            }
//        }
//        return authModel;
//    }
//
//
//    public Response register(LoginModel loginModel) {
//        Response response = new Response();
//        if(loginModel.getEmail() != null && loginModel.getPassword() != null && loginModel.getUserName() != null) {
//            String hashedPassword = passwordEncoder.encode(loginModel.getPassword()); // Hash password
//            LoginEntity loginEntity = new LoginEntity();
//            loginEntity.setUserName(loginModel.getUserName());
//            loginEntity.setEmail(loginModel.getEmail());
//            loginEntity.setPassword(hashedPassword);
//            loginRepository.save(loginEntity);
//            response.setMessage("SUCCESS");
//            response.setStatus(Boolean.FALSE);
//        }
//        return response;
//    }
//
//    public Response forgotPassword(ForgotPasswordModel forgotPasswdModel) {
//        Response response = new Response();
//        if(forgotPasswdModel.getEmail() != null && forgotPasswdModel.getNewPassword() != null && forgotPasswdModel.getConformPassword() != null ) {
////            String hashedPassword = passwordEncoder.encode(loginModel.getPassword()); // Hash password
////            LoginEntity loginEntity = new LoginEntity();
////            loginEntity.setUserName(loginModel.getUserName());
////            loginEntity.setEmail(loginModel.getEmail());
////            loginEntity.setPassword(hashedPassword);
////            loginRepository.save(loginEntity);
//            response.setMessage("SUCCESS");
//            response.setStatus(Boolean.FALSE);
//        }
//        return response;
//
//    }
}
