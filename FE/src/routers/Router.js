import React, { Suspense } from "react";
import "./Router.css";
import { Route, Routes, useLocation } from "react-router-dom";
import { TransitionGroup, CSSTransition } from "react-transition-group";
import Main from "../screens/Main.js";
import Analysis from "../screens/analysis/Analysis";
import Ranking from "../screens/Ranking.js";
import SignIn from "../screens/SignIn";
import SignUp from "../screens/SignUp.js";
import MyPage from "../screens/MyPage.js";
import { useState, useEffect } from "react";
import OnSocialLogin from "../screens/sign/SocialLogin";
import RankingDetail from "../screens/RankingDetail";

function Router({ getUserData }) {
  const location = useLocation();
  return (
    <div className="router">
      <Suspense>
        <TransitionGroup className="transition-group">
          <CSSTransition
            key={location.pathname === "/ranking" ? location.pathname : null}
            classNames="sidefade"
            timeout={1000}
          >
            <Routes location={location}>
              <Route
                path="/:params"
                element={<OnSocialLogin getUserData={getUserData} />}
              />
              <Route path="/" element={<Main />} />
              <Route path="/ranking" element={<Ranking />}></Route>
              <Route path="/ranking/detail/:id" element={<RankingDetail />} />

              <Route path="/signin" element={<SignIn />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/mypage" element={<MyPage />} />
              <Route path="/anal" element={<Analysis />} />
            </Routes>
          </CSSTransition>
        </TransitionGroup>
      </Suspense>
    </div>
  );
}

export default Router;
