import React, {Suspense} from 'react';
import logo from "./logo.svg";
import {Trans, useTranslation, withTranslation} from "react-i18next";
import {LegacyWelcomeClass} from "./LegacyWelcomeClass";

const Welcome = withTranslation()(LegacyWelcomeClass);

const TransComponent = () => (
    <div style={{background: '#ABFFAB'}}>
        Trans:
        <Trans i18nKey="description.part3">
            To get started, edit <code>src/App.js</code> and save to reload. ::SampleComponent::
        </Trans>
        <br/>
        Trans namespace:
        <Trans i18nKey="main:trans.description">
            (With namespace) To get started, edit <code>src/App.js</code> and save to reload. ::SampleComponent::NS::
        </Trans>
    </div>
);

const UseTranslationHookComponent = () => {
    const { t, i18n } = useTranslation('hook');
    return (
        <div style={{background: '#32F0AB'}}>
            <div>useTranslation hook:</div>
            <div>{t('example.title')}</div>
        </div>
    );
};

const MainComponent = () => {
    const { t, i18n } = useTranslation();

    const changeLanguage = lng => {
        i18n.changeLanguage(lng);
    };

    return (
        <div className="App">
            <div className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <Welcome />
                <button onClick={() => changeLanguage('de')}>de</button>
                <button onClick={() => changeLanguage('en')}>en</button>
            </div>
            <div className="App-intro">
                <TransComponent/>
                <UseTranslationHookComponent/>
            </div>
            <div>{t('description.part2')}</div>
            <div>{'Namespace: ' + t('main:header.title')}</div>
        </div>
    );
}

// loading component for suspense fallback
const Loader = () => (
    <div className="App">
        <img src={logo} className="App-logo" alt="logo" />
        <div>loading...</div>
    </div>
);

export const MainPage = () => (
    <Suspense fallback={<Loader />}>
        <MainComponent/>
    </Suspense>
);