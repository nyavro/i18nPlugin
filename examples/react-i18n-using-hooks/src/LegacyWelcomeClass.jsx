import React, {Component} from 'react';

export class LegacyWelcomeClass extends Component {
    render() {
        const {t} = this.props;
        return <h2>{t('title')}</h2>;
    }
}