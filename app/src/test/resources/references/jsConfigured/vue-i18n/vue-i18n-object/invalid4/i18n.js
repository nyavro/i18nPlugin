import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Formatter from './formatter'

Vue.use(VueI18n)

const locale = 'en-US' // default locale
const formatter = new Formatter({ locale })

const hello = {
  short: 'hello!!',
  full: 'hey there!!!'
};

const hello1 = hello;
const hello2 = hello1;
const hello3 = hello2;
const hello4 = hello3;
const hello5 = hello4;
const hello6 = hello5;
const hello7 = hello6;
const hello8 = hello7;
const hello9 = hello8;
const hello10 = hello9;
const hello11 = hello10;

export default new VueI18n({
  locale,
  formatter,
  messages: {
    'en-US': {
      tos: 'Terms of service',
      terms: 'I accept the {0}',
      message: {
        hello: hello11,
        plural: 'You have {n, plural, =0{no messages} one{1 message} other{# messages}}.',
        select: '{gender, select, male{He} female{She} other{They}} liked this.',
        number: 'Current Percent: {current, number, percent}',
        time: 'Current Time: {current, time, short}',
      },
      fly: {
        departure: 'You can {action} until {limit} minutes from departure',
        board: 'board'
      }
    }
  }
})
