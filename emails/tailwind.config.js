/** @type {import('tailwindcss').Config} */
module.exports = {
  presets: [
    require('tailwindcss-preset-email'),
  ],
  content: [
    './components/**/*.html',
    './emails/**/*.html',
    './layouts/**/*.html',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#1A4D3E',
          container: '#1B4D3E',
        },
        'on-primary': {
          DEFAULT: '#FFFFFF',
          container: '#8ABDA9',
        },
        secondary: {
          DEFAULT: '#57605D',
          container: '#E9F3EE',
        },
        'on-secondary': {
          DEFAULT: '#FFFFFF',
          container: '#393E3A',
        },
        tertiary: {
          DEFAULT: '#266747',
          container: '#41815E',
        },
        'on-tertiary': {
          DEFAULT: '#FFFFFF',
          container: '#F6FFF5',
        },
        background: '#FAF9F6',
        'on-background': '#8B8680',
        surface: '#FFFFFF',
        'on-surface': {
          DEFAULT: '#1C1B1B',
          variant: '#404945',
        },
        'surface-variant': '#DCE5DF',
        outline: {
          DEFAULT: '#DCE5DF',
          subtle: '#E8E8E8',
        },
        error: {
          DEFAULT: '#AE282C',
        },
        'on-error': '#FFFFFF',
      },
      fontFamily: {
        display: ["'Domine'", 'ui-serif', 'Georgia', 'Cambria', '"Times New Roman"', 'Times', 'serif'],
        body: ['"Inter"', 'ui-sans-serif', 'system-ui', '-apple-system', '"Segoe UI"', 'Roboto', 'sans-serif'],
      },
      screens: {
        screen: { raw: 'screen' },
      },
      borderRadius: {
        pill: '999px',
      },
    },
  },
}