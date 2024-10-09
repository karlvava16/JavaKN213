const initialState = {
    auth: {
        token: null,
        user: null
    },
    page: 'home',
};

const AppContext = React.createContext(null);

function reducer( state, action ) {
    switch( action.type ) {
        case 'navigate':
            window.location.hash = action.payload;
            return { ...state,
                page: action.payload,
            };
            case 'authenticate':
                window.localStorage.setItem("auth-user", JSON.stringify(action.payload));
                return { ...state,
                    page: action.payload,
                };
    }
}

function App({contextPath}) {
    const [state, dispatch] = React.useReducer( reducer, initialState );
    React.useEffect( () => {
        let hash = window.location.hash;
        if( hash.length > 1 ) {
            dispatch( { type: "navigate", payload: hash.substring(1) } );
        }
    }, [] );
    return <AppContext.Provider value={{state, dispatch, contextPath}}>
        <header>
            <nav className="navbar navbar-expand-lg bg-body-tertiary">
                <div className="container-fluid">
                    <a className="navbar-brand" onClick={() => dispatch({type: "navigate", payload: "home"})}>Крамниця</a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <a className="nav-link"
                                   onClick={() => dispatch({type: "navigate", payload: "home"})}>Home</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link "
                                   onClick={() => dispatch({type: "navigate", payload: "cart"})}>Кошик</a>
                            </li>
                        </ul>
                        <form className="d-flex m-0 mr-1" role="search">
                            <input className="form-control me-2" type="search" placeholder="Search"
                                   aria-label="Search"/>
                            <button className="btn btn-outline-success" type="submit"><i className="bi bi-search"></i>
                            </button>
                        </form>
                        <button type="button" className="btn btn-primary mr-1" data-bs-toggle="modal"
                                data-bs-target="#staticBackdrop">
                            <i className="bi bi-box-arrow-in-right"></i>
                        </button>
                    </div>
                </div>
            </nav>
        </header>
        <main className="container">
            {state.page === 'home' && <Home/>}
            {state.page === 'cart' && <Cart/>}
        </main>
        <div className="spacer"></div>



        <div className="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
            <AuthModal/>
        </div>
        
        <footer className="bg-body-tertiary px-3 py-2">
            &copy; 2024, ITSTEP KN-P-213
        </footer>



    </AppContext.Provider>;
}

function Cart() {
    const {state, dispatch} = React.useContext(AppContext);
    return <div>
        <h2>Кошик</h2>
        <b onClick={() => dispatch({type: "navigate", payload: "home"})}>На Домашню</b>
    </div>;
}

function Home() {
    const {state, dispatch} = React.useContext(AppContext);
    return <div>
        <h2>Домашня</h2>
        <b onClick={() => dispatch({type: "navigate", payload: "cart"})}>До Кошику</b>
    </div>;
}


function AuthModal() {
    const {contextPath, dispatch} = React.useContext(AppContext);
    const [login, setLogin] = React.useState("");
    const [password, setPassword] = React.useState("");

    const authClick = React.useCallback(() => {
        console.log(login, password);
        fetch(`${contextPath}/auth`, {
            method: "GET",
            headers: {
                'Authorization': 'Basic ' + btoa(login + ':' + password),
            }
        }).then(res => res.json()).then(j =>
        {
            if(j.status === "Ok")
            {
                // j.data - дані про користувача, токен та права (роль)
                // задача: зберегти ці дані і використовувати без повторної автентифікації
                // куди можна зберігати? а) state/context б) sessionStorage в) localStorage
                dispatch({type: 'authenticate', payload: j.data});
            }
            else
            {
                alert(j.data)
            }
        });
    })
    return <div className="modal-dialog">
        <div className="modal-content">
            <div className="modal-header">
                <h1 className="modal-title fs-5" id="staticBackdropLabel">Modal title</h1>
                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div className="modal-body">
                <div className="input-group mb-3"><span className="input-group-text" id="login-addon">
                    <i className="bi bi-person-fill-lock"></i></span>
                    <input onChange={e => setLogin(e.target.value)}
                    type="text" className="form-control" placeholder="Логін" aria-label="Login"
                    aria-describedby="login-addon"/></div>
                <div className="input-group mb-3"><span className="input-group-text" id="password-addon"><i
                    className="bi bi-key-fill"></i></span>
                    <input onChange={e => setPassword(e.target.value)}
                        type="password" className="form-control" placeholder="Пароль" aria-label="Password"
                    aria-describedby="password-addon"/></div>
            </div>
            <div className="modal-footer">
                <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Скасувати</button>
                <button type="button" className="btn btn-primary" onClick={authClick}>Вхід</button>
            </div>
        </div>
    </div>
}

const domRoot = document.getElementById("app-container");
const cp = domRoot.getAttribute("data-context-path");
const hp = domRoot.getAttribute("data-home-path");

// console.log(cp);
ReactDOM
    .createRoot(domRoot)
    .render(<App contextPath={cp} homePath={hp}/>);
